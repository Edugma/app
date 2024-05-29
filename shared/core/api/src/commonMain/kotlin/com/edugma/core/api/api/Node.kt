package com.edugma.core.api.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Node(
    @SerialName("name")
    val name: String,
    @SerialName("other_names")
    val otherNames: List<String>,
    @SerialName("image")
    val image: String,
    @SerialName("contract")
    val contract: String,
)
