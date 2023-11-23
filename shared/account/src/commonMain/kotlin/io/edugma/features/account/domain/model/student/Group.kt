package io.edugma.features.account.domain.model.student

@kotlinx.serialization.Serializable
data class Group(
    val id: String,
    val title: String,
    val course: Int? = null,
    val faculty: String?,
)
