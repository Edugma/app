package io.edugma.domain.schedule.model.group

import kotlinx.serialization.Serializable

@Serializable
data class GroupInfo(
    val id: String,
    val title: String,
    val description: String,
    val course: String,
    val isEvening: Boolean
) : Comparable<GroupInfo> {
    override fun compareTo(other: GroupInfo): Int {
        return title.compareTo(other.title)
    }
}