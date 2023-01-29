package io.edugma.features.schedule.domain.model

@kotlinx.serialization.Serializable
data class StudentDirection(
    val id: String,
    val title: String,
    val code: String,
)
