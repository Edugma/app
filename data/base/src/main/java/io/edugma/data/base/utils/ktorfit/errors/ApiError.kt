package io.edugma.data.base.utils.ktorfit.errors

/**
 * Failure response with body
 */
data class ApiError(val body: Any, val code: Int) : Exception()
