package io.edugma.data.base.utils.retrofit.network.errors

/**
 * For example, json parsing error
 */
data class UnknownResponseError(val error: Throwable?) : Exception()
