package io.edugma.features.account.domain.model.ui

@kotlinx.serialization.Serializable
data class Label(
    val id: String,
    val text: String,
    val icon: String,
)
