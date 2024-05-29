package com.edugma.features.account.domain.model.student

import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val name: String,
    val avatar: String? = null,
    val faculty: String?,
    val group: Group? = null,
    val course: Int? = null,
) {
    fun getInfo() = "Студент $course курса" + group?.let { " ${it.title} группы" }.orEmpty()
}
