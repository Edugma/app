package io.edugma.features.account.domain.model.student

@kotlinx.serialization.Serializable
data class StudentDirection(
    val id: String,
    val title: String,
    val code: String,
)
