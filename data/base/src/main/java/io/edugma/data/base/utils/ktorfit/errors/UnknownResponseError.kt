package io.edugma.data.base.utils.ktorfit.errors

/**
 * For example, json parsing error
 */
data class UnknownResponseError(val error: Throwable?) : Exception(error)
