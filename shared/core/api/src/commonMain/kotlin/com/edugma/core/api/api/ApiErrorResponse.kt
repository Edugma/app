package com.edugma.core.api.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    @SerialName("errors")
    val errors: List<ApiError>,
)

@Serializable
data class ApiError(
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("details")
    val data: Map<String, String>? = null,
)
