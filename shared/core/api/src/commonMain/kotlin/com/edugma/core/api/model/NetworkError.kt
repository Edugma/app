package com.edugma.core.api.model

import com.edugma.core.api.api.ApiError
import io.ktor.serialization.JsonConvertException
import kotlinx.io.IOException

sealed interface ResponseError {
    /**
     *  Represent IOExceptions and connectivity issues.
     */
    data class NetworkError(val error: IOException) : Exception(error), ResponseError

    /**
     * Represent SerializationExceptions. Eg, json parsing error.
     */
    data class UnknownResponseError(val error: Throwable?) : Exception(error), ResponseError

    data class SerializationError(val error: JsonConvertException) : Exception(error), ResponseError

    /**
     * Failure response with body.
     * Represents server (50x) and client (40x) errors.
     */
    data class HttpError(val body: List<ApiError>?, val code: Int) : Exception(), ResponseError
}
