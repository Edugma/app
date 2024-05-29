package com.edugma.core.api.repository

import io.ktor.http.HttpMethod

interface UrlRepository {
    fun url(name: String, params: Map<String, String>): String
    fun queryParams(name: String, params: Map<String, String>): Map<String, String>
    fun headerParams(name: String, params: Map<String, String>): Map<String, String>
    fun getMethod(name: String): HttpMethod
    fun isSecure(name: String): Boolean
}
