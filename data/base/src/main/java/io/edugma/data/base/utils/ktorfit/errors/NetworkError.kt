package io.edugma.data.base.utils.ktorfit.errors

import java.io.IOException

/**
 * Network error
 */
data class NetworkError(val error: IOException) : Exception(error)
