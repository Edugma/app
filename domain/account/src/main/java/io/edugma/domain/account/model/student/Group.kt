package io.edugma.domain.account.model.student

@kotlinx.serialization.Serializable
data class Group(
    val id: String,
    val title: String,
    val course: Int? = null,
    val faculty: StudentFaculty,
    val direction: StudentDirection,
)
