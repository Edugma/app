package io.edugma.features.account.domain.model.student

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
    val group: Group? = null,
    val specialization: StudentSpecialization? = null,
    val educationType: String,
    val educationForm: String,
    val payment: String,
    val course: Int? = null,
    val years: String,
    var code: String,
    var status: String,
    var branch: StudentBranch,
) {
    fun getFullName() = "$lastName $firstName $middleName"

    fun getInfo() = (if (sex == "Женский") "Студентка" else "Студент") +
        " $course курса" + group?.let { " ${it.title} группы" }.orEmpty()
}
