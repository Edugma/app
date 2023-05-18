package io.edugma.domain.account.model.ui

@kotlinx.serialization.Serializable
data class Label(
    val id: String,
    val text: String,
    val icon: String
)
