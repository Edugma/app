package com.edugma.core.network

import com.edugma.core.api.api.CrashAnalytics
import com.edugma.core.api.model.ResponseError
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException

class ResultConverterFactory : Converter.Factory {

    override fun suspendResponseConverter(
        typeData: TypeData,
        ktorfit: Ktorfit,
    ): Converter.SuspendResponseConverter<HttpResponse, *>? {
        if (typeData.typeInfo.type == Result::class) {
            return object : Converter.SuspendResponseConverter<HttpResponse, Any> {
                override suspend fun convert(result: KtorfitResult): Any {
                    return when (result) {
                        is KtorfitResult.Failure -> {
                            convertFailure(result.throwable)
                        }
                        is KtorfitResult.Success -> {
                            convertSuccess(result.response, typeData)
                        }
                    }
                }

                private fun convertFailure(error: Throwable) =
                    when (error) {
                        is IOException -> ResponseError.NetworkError(error)
                        else -> ResponseError.UnknownResponseError(error)
                    }

                private suspend fun convertSuccess(
                    response: HttpResponse,
                    typeData: TypeData,
                ): Result<Any> {
                    return try {
                        if (response.status.isSuccess()) {
                            val body = response.body<Any>(typeData.typeArgs.first().typeInfo)
                            Result.success<Any>(body)
                        } else {
                            val body = runCatching {
                                response.body<Any>(typeData.typeArgs.first().typeInfo)
                            }.getOrNull()
                            val e = ResponseError.HttpError(body, response.status.value)
                            CrashAnalytics.logException(TAG, "wrapSuspendResponse: ", e)
                            Result.failure<Any>(e)
                        }
                    } catch (e: Throwable) {
                        val error = when (e) {
                            is JsonConvertException -> ResponseError.SerializationError(e)
                            else -> ResponseError.UnknownResponseError(e)
                        }

                        CrashAnalytics.logException(TAG, "wrapSuspendResponse: ", error)
                        Result.failure<Any>(error)
                    }
                }
            }
        }
        return null
    }

    companion object {
        private const val TAG = "ResultConverter"
    }
}
