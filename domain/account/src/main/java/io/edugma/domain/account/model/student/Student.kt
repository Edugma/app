package io.edugma.domain.account.model.student

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    val id: String,
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val sex: String? = null,
    val avatar: String? = null,
    val birthday: LocalDate? = null,
    val group: Group? = null,
    val specialization: StudentSpecialization? = null,
    val educationType: String,
    val educationForm: String,
    val payment: String,
    val course: Int? = null,
    val years: String,
    var code: String,
    var dormitory: String? = null,
    var dormitoryRoom: String? = null,
    var branch: StudentBranch,
) {
    fun getFullName() = "$lastName $firstName $middleName"

    fun getInfo() = "Студент $course курса" + group?.let { " ${it.title} группы" }.orEmpty() +
        ", ${educationForm.lowercase()} форма обучения"

    fun getFaculty() = group?.let { "${it.faculty.titleShort}, (${it.faculty.title})" }
}
