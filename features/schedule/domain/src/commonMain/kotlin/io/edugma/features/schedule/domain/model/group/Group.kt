package io.edugma.features.schedule.domain.model.group

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val id: String,
    val title: String,
    val description: String,
) : Comparable<Group> {
    override fun compareTo(other: Group): Int {
        return title.compareTo(other.title)
    }
}
