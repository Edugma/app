package io.edugma.core.network.errors

import io.ktor.utils.io.errors.IOException

/**
 * Network error
 */
data class NetworkError(val error: IOException) : Exception(error)
