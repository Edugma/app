package io.edugma.domain.schedule.model

import io.edugma.domain.schedule.model.group.GroupInfo
import kotlinx.serialization.Serializable

@Serializable
data class StudentShort(
    val id: String,
    val name: String,
    val avatar: String?,
    val group: GroupInfo?,
    val course: Int?,
)
