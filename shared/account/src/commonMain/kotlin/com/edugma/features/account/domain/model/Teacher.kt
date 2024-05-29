package com.edugma.features.account.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val grade: String? = null,
    val department: String? = null,
    val email: String? = null,
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
            department?.let { append(prefix + it) }
        }
    }
