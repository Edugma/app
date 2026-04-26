package com.edugma.core.api.api

import com.edugma.core.api.model.ResponseError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

suspend inline fun <reified T> HttpClient.convert(
    responseResult: Result<HttpResponse>,
): Result<T> {
    return convertInternal(responseResult = responseResult, typeInfo = typeInfo<T>())
}

@Suppress("UNCHECKED_CAST")
suspend fun <T> HttpClient.convertInternal(
    responseResult: Result<HttpResponse>,
    typeInfo: TypeInfo,
): Result<T> {
    return try {
        val response = responseResult.getOrThrow()
        if (response.status.isSuccess()) {
            val body = response.call.bodyNullable(typeInfo)
            Result.success<T>(body as T)
        } else {
            var body: List<ApiError>? = runCatching {
                response.body<ApiErrorResponse>()
            }.getOrNull()?.errors

            if (body == null) {
                val errorText = response.bodyAsText()
                if (errorText.isNotBlank()) {
                    body = listOf(ApiError(code = "UNKNOWN_SERVER_ERROR", message = errorText))
                }
            }

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
