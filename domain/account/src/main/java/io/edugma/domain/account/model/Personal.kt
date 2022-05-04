package io.edugma.domain.account.model

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
    val specialization: String,
    val degreeLength: String,
    val educationForm: String,
    val finance: String,
    val degreeLevel: String,
    val enterYear: String,
    val orders: List<String>,
    val subdivisions: List<Subdivision>? = null
)

@Serializable
data class Subdivision(
    val category: String,
    val jobType: String? = null,
    val status: String? = null,
    val subdivision: String? = null,
    val wage: String? = null,
)

//data class Personal(
//    val name: String,
//    val type: EducationType,
//    val avatarUrl: String?,
//    val course: Int,
//    val group: String,
//    val direction: String?,
//    val faculty: String,
//    val dormitory: String?,
//    val dormitoryRoom: String?,
//    val isPaid: Boolean,
//    val startYear: Int?,
//    val endYear: Int?,
//    @Serializable(LocalDateConverter::class)
//    val startDate: LocalDate?,
//    @Serializable(LocalDateConverter::class)
//    val endDate: LocalDate?
//)
