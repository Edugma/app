package com.edugma.data.schedule.model

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleComplexRequest(
    val typesId: List<String>,
    val subjectsId: List<String>,
    val teachersId: List<String>,
    val groupsId: List<String>,
    val placesId: List<String>,
)
