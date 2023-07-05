package io.edugma.features.account.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val stuffType: String? = null,
    val grade: String? = null,
    val departmentParent: io.edugma.features.account.domain.model.Department? = null,
    val department: io.edugma.features.account.domain.model.Department? = null,
    val email: String? = null,
    val sex: String? = null,
    val birthday: LocalDate? = null,
    val dialogId: String? = null,
) : Comparable<Teacher> {
    override fun compareTo(other: Teacher): Int {
        return name.compareTo(other.name)
    }
}

val Teacher.description: String
    get() {
        return buildString {
            grade?.let { append(it) }
            var prefix = ""
            if (isNotEmpty()) {
                prefix = ", "
            }
            department?.title?.let { append(prefix + it) }
                ?: departmentParent?.title?.let { append(prefix + it) }
        }
    }

val Teacher.departments: String
    get() {
        return buildString {
            departmentParent?.title?.let { append(it) }
            var prefix = ""
            if (isNotEmpty()) {
                prefix = ", "
            }
            department?.title?.let { append(prefix + it) }
        }
    }
