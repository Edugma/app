package io.edugma.core.network

import co.touchlab.kermit.Logger
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.SuspendResponseConverter
import de.jensklingenberg.ktorfit.internal.TypeData
import io.edugma.core.network.errors.ApiError
import io.edugma.core.network.errors.ConvertError
import io.edugma.core.network.errors.NetworkError
import io.edugma.core.network.errors.UnknownResponseError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.JsonConvertException
import io.ktor.util.reflect.TypeInfo
import java.io.IOException

class ResultConverter : SuspendResponseConverter {

    companion object {
        private const val TAG = "ResultConverter"
    }

    override suspend fun <RequestType> wrapSuspendResponse(
        typeData: TypeData,
        requestFunction: suspend () -> Pair<TypeInfo, HttpResponse>,
        ktorfit: Ktorfit,
    ): Any {
        return try {
            val (info, response) = requestFunction()
            val body = response.body<RequestType>(info)

            if (response.status.isSuccess()) {
                Result.success<RequestType>(body)
            } else {
                val e = ApiError(body as Any, response.status.value)
                Logger.e("wrapSuspendResponse: ", e, tag = TAG)
                Result.failure<RequestType>(e)
            }
        } catch (e: Throwable) {
            val error = when (e) {
                is IOException -> NetworkError(e)
                is JsonConvertException -> ConvertError(e)
                else -> UnknownResponseError(e)
            }

            Logger.e("wrapSuspendResponse: ", error, tag = TAG)
            Result.failure<RequestType>(error)
        }
    }

    override fun supportedType(typeData: TypeData, isSuspend: Boolean): Boolean {
        return true
    }
}
