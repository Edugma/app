package io.edugma.core.network.errors

import java.io.IOException

/**
 * Network error
 */
data class NetworkError(val error: IOException) : Exception(error)
