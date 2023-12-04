package io.edugma.features.schedule.domain.model.teacher

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class TeacherInfo(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val stuffType: String? = null,
    val grade: String? = null,
    val email: String? = null,
    val sex: String? = null,
    val birthday: LocalDate? = null,
) : Comparable<TeacherInfo> {
    override fun compareTo(other: TeacherInfo): Int {
        return name.compareTo(other.name)
    }
}

val TeacherInfo.description: String
    get() {
        return buildString {
            grade?.let { append(it) }
        }
    }
