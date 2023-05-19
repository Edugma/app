package io.edugma.data.base.utils.ktorfit.errors

import io.ktor.serialization.JsonConvertException

data class ConvertError(val error: JsonConvertException) : Exception(error)
