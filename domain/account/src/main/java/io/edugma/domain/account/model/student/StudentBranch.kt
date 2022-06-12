package io.edugma.domain.account.model.student

@kotlinx.serialization.Serializable
data class StudentBranch(
    val id: String,
    val title: String
)