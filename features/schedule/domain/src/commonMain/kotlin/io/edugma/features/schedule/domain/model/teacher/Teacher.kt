package io.edugma.features.schedule.domain.model.teacher

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val description: String,
) : Comparable<Teacher> {
    override fun compareTo(other: Teacher): Int {
        return name.compareTo(other.name)
    }
}
