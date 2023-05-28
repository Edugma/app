package io.edugma.core.network.errors

import io.ktor.serialization.JsonConvertException

data class ConvertError(val error: JsonConvertException) : Exception(error)
