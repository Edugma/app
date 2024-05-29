package com.edugma.features.account.domain.model.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Performance(
    @SerialName("id")
    val id: String,
    @SerialName("grades")
    val grades: List<GradePosition>,
)
