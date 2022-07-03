package io.edugma.domain.schedule.model.teacher

import io.edugma.domain.schedule.model.Department
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class TeacherInfo(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val stuffType: String? = null,
    val grade: String? = null,
    val departmentParent: Department? = null,
    val department: Department? = null,
    val email: String? = null,
    val sex: String? = null,
    val birthday: LocalDate? = null
) : Comparable<TeacherInfo> {
    override fun compareTo(other: TeacherInfo): Int {
        return name.compareTo(other.name)
    }
}

val TeacherInfo.description: String
    get() {
        return buildString {
            grade?.let { append(it) }

            department?.let {
                if (isNotEmpty()) append(", ")
                append(department.title)
            }

            departmentParent?.let {
                if (isNotEmpty()) append(", ")
                append(departmentParent.title)
            }
        }
    }