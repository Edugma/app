package io.edugma.domain.account.model.student

@kotlinx.serialization.Serializable
data class StudentSpecialization(
    val id: String,
    val title: String,
)
