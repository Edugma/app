package io.edugma.domain.schedule.model.teacher

import kotlinx.serialization.Serializable

@Serializable
data class TeacherInfo(
    val id: String,
    val name: String,
    val description: String
) : Comparable<TeacherInfo> {
    override fun compareTo(other: TeacherInfo): Int {
        return name.compareTo(other.name)
    }
}