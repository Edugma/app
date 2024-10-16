package com.edugma.features.schedule.domain.model

@kotlinx.serialization.Serializable
data class StudentFaculty(
    val id: String,
    val title: String,
    val titleShort: String? = null,
)
