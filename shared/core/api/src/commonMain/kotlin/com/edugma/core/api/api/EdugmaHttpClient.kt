package com.edugma.core.api.api

import co.touchlab.kermit.Logger
import com.edugma.core.api.model.ResponseError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.CancellationException

interface EdugmaHttpClient {

    suspend fun getInternal(name: String, builder: Builder): HttpResponse

    suspend fun postInternal(name: String, builder: PostBuilder): HttpResponse

    open class Builder() {
        var paramsMap = hashMapOf<String, String>()
        var version: String = ""
        fun param(name: String, value: Any?) {
            paramsMap[name] = value?.toString().orEmpty()
        }

        // TODO api version
        fun version(v: String, builder: Builder.() -> Unit) {
            this.version = v
        }
    }

    open class PostBuilder : Builder() {
        var body: Any? = null
        var typeInfo: TypeInfo? = null

        @PublishedApi
        internal fun body(value: Any?, typeInfo: TypeInfo) {
            this.body = value
            this.typeInfo = typeInfo
        }

        inline fun <reified T> body(body: T) {
            body(body, typeInfo<T>())
        }
    }
}

suspend inline fun <reified T> EdugmaHttpClient.get(
    name: String,
    build: EdugmaHttpClient.Builder.() -> Unit = {},
): T {
    val resultResponse = runCatching {
        getInternal(name, EdugmaHttpClient.Builder().apply(build))
    }
    return convert<T>(resultResponse, typeInfo<T>()).getOrThrow()
}

suspend inline fun <reified T> EdugmaHttpClient.getResult(
    name: String,
    build: EdugmaHttpClient.Builder.() -> Unit = {},
): Result<T> {
    val resultResponse = runCatching {
        getInternal(name, EdugmaHttpClient.Builder().apply(build))
    }
    return convert<T>(resultResponse, typeInfo<T>())
}

suspend inline fun <reified T> EdugmaHttpClient.post(
    name: String,
    build: EdugmaHttpClient.PostBuilder.() -> Unit = {},
): T {
    val resultResponse = runCatching {
        postInternal(name, EdugmaHttpClient.PostBuilder().apply(build))
    }
    return convert<T>(resultResponse, typeInfo<T>()).getOrThrow()
}

suspend inline fun <reified T> EdugmaHttpClient.postResult(
    name: String,
    build: EdugmaHttpClient.PostBuilder.() -> Unit = {},
): Result<T> {
    val resultResponse = runCatching {
        postInternal(name, EdugmaHttpClient.PostBuilder().apply(build))
    }
    return convert(resultResponse, typeInfo<T>())
}

// TODO replace
private class TempError

@Suppress("UNCHECKED_CAST")
suspend fun <T> EdugmaHttpClient.convert(
    responseResult: Result<HttpResponse>,
    typeInfo: TypeInfo,
): Result<T> {
    return try {
        val response = responseResult.getOrThrow()
        if (response.status.isSuccess()) {
            val body = response.call.bodyNullable(typeInfo)
            Result.success<T>(body as T)
        } else {
            val body: Any = runCatching {
                response.body<TempError>()
            }.getOrNull() ?: response.bodyAsText()
            
            val e = ResponseError.HttpError(body, response.status.value)
            CrashAnalytics.logException(TAG, "wrapSuspendResponse: response status error", e)
            Result.failure<T>(e)
        }
    } catch (e: Throwable) {
        val error = when (e) {
            is CancellationException -> throw e
            is JsonConvertException -> ResponseError.SerializationError(e)
            is IOException -> ResponseError.NetworkError(e)
            else -> ResponseError.UnknownResponseError(e)
        }

        CrashAnalytics.logException(TAG, "wrapSuspendResponse: ", error)
        Result.failure<T>(error)
    }
}

private const val TAG = "ResultConverter"
