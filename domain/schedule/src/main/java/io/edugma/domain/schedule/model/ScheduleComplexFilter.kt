package io.edugma.domain.schedule.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleComplexFilter(
    val typesId: List<String> = emptyList(),
    val subjectsId: List<String> = emptyList(),
    val teachersId: List<String> = emptyList(),
    val groupsId: List<String> = emptyList(),
    val placesId: List<String> = emptyList(),
)
