package io.edugma.features.account.domain.model

import io.edugma.features.account.domain.model.ui.Label
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Personal(
    val id: Int,
    val userStatus: String,
    val status: String,
    val course: String,
    val name: String,
    val surname: String,
    val patronymic: String,
    val avatar: String,
    val birthday: String,
    val sex: String,
    val code: String,
    val faculty: String,
    val group: String,
    val specialty: String,
    val specialization: String? = null,
    val degreeLength: String,
    val educationForm: String,
    val finance: String,
    val degreeLevel: String,
    val enterYear: String,
    val orders: List<Order>,
    val subdivisions: List<Subdivision>? = null,
    val applications: List<Application> = emptyList(),
    val labels: List<Label> = emptyList(),
) {
    fun getFullName() = "$surname $name $patronymic"
    fun getNameSurname() = "$name $surname"

    val initials = "${name.firstOrNull() ?: ""}${surname.firstOrNull() ?: ""}"
}

@Serializable
data class Subdivision(
    val category: String,
    val jobType: String? = null,
    val status: String? = null,
    val subdivision: String? = null,
    val wage: String? = null,
)

@Serializable
data class Order(
    val date: LocalDate? = null,
    val name: String,
    val description: String,
)