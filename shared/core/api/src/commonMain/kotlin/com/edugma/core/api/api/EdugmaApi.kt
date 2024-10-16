package com.edugma.core.api.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EdugmaApi(
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: String,
    @SerialName("variables")
    val variables: Map<String, String>,
    @SerialName("endpoints")
    val endpoints: Map<String, Path>,
)
