package io.edugma.features.account.domain.model.performance

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class Performance(
    val id: Int,
    val billNum: String,
    val billType: String? = null,
    val docType: String,
    val name: String,
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val grade: String,
    val ticketNum: String? = null,
    val teacher: String,
    val course: Int,
    val semester: Int,
    val examType: String,
    val chair: String,
)
