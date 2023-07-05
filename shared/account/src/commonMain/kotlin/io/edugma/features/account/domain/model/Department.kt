package io.edugma.features.account.domain.model

@kotlinx.serialization.Serializable
data class Department(
    val id: String,
    val title: String,
)
