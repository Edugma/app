package com.edugma.core.api.utils

import io.ktor.http.parseUrl

fun isUrl(value: String): Boolean {
    return parseUrl(value) != null
}
