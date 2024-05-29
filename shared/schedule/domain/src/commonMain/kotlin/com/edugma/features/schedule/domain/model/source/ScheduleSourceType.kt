package com.edugma.features.schedule.domain.model.source

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleSourceType(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
) {
    companion object {
        const val FAVORITE = "_favorite"
        const val COMPLEX = "_complex"
    }
}
