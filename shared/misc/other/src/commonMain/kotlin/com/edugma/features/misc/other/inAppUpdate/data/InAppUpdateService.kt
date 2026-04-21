package com.edugma.features.misc.other.inAppUpdate.data

import com.edugma.core.api.api.convert
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class InAppUpdateService(
    private val client: HttpClient,
) {
    suspend fun getLastVersion(): Result<Version> {
        val baseUrl = "https://raw.githubusercontent.com/Edugma/resources/main/"
        val url = baseUrl + "versions/android/last-version.json"
        val response = runCatching { client.get(url) }
        return client.convert<Version>(response)
    }
}
