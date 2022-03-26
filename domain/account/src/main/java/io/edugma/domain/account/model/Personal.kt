package io.edugma.domain.account.model

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Personal(
    val name: String,
    val type: EducationType,
    val avatarUrl: String?,
    val course: Int,
    val group: String,
    val direction: String?,
    val faculty: String,
    val dormitory: String?,
    val dormitoryRoom: String?,
    val isPaid: Boolean,
    val startYear: Int?,
    val endYear: Int?,
    @Serializable(LocalDateConverter::class)
    val startDate: LocalDate?,
    @Serializable(LocalDateConverter::class)
    val endDate: LocalDate?
)
