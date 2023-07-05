package io.edugma.features.account.domain.model.student

@kotlinx.serialization.Serializable
data class StudentFaculty(
    val id: String,
    val title: String,
    val titleShort: String? = null,
)
