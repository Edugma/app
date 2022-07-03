package io.edugma.domain.schedule.model

@kotlinx.serialization.Serializable
data class StudentDirection(
    val id: String,
    val title: String,
    val code: String
)