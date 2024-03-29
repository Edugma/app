package io.edugma.core.api.model

import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException

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
    data class HttpError(val body: Any?, val code: Int) : Exception(), ResponseError
}
