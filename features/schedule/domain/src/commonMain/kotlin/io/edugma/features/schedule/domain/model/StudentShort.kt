package io.edugma.features.schedule.domain.model

import io.edugma.features.schedule.domain.model.group.GroupInfo
import kotlinx.serialization.Serializable

@Serializable
data class StudentShort(
    val id: String,
    val name: String,
    val avatar: String?,
    val group: GroupInfo?,
    val course: Int?,
)
