package io.edugma.core.network.errors

/**
 * For example, json parsing error
 */
data class UnknownResponseError(val error: Throwable?) : Exception(error)
