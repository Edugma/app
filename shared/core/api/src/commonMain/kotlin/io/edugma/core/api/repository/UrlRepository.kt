package io.edugma.core.api.repository

interface UrlRepository {
    fun url(name: String, params: Map<String, String>): String
    fun queryParams(name: String, params: Map<String, String>): Map<String, String>
    fun headerParams(name: String, params: Map<String, String>): Map<String, String>
}
