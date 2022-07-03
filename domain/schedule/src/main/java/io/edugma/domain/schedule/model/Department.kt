package io.edugma.domain.schedule.model

@kotlinx.serialization.Serializable
data class Department(
    val id: String,
    val title: String
)