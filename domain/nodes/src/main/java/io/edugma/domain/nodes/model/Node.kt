package io.edugma.domain.nodes.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Node(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("other_names")
    val otherNames: List<String>,
    @SerialName("image")
    val image: String,
    @SerialName("contract")
    val contract: String
)