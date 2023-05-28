package io.edugma.core.network.errors

/**
 * Failure response with body
 */
data class ApiError(val body: Any, val code: Int) : Exception()
