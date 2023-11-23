package io.edugma.core.api.api

import kotlinx.serialization.Serializable

@Serializable
data class Path(
    val url: String,
    val queryParams: Map<String, String>? = null,
    val headerParams: Map<String, String>? = null,
    val version: String? = null,
)
