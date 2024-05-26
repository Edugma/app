package io.edugma.core.api.api

import io.ktor.http.HttpMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Path(
    @SerialName("method")
    val method: String,
    @SerialName("url")
    val url: String,
    @SerialName("queryParams")
    val queryParams: Map<String, String>? = null,
    @SerialName("headerParams")
    val headerParams: Map<String, String>? = null,
    @SerialName("version")
    val version: String? = null,
    @SerialName("security")
    val security: Boolean = false,
)
