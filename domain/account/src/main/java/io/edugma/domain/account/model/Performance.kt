package io.edugma.domain.account.model

import io.edugma.domain.base.utils.converters.LocalDateConverter
import io.edugma.domain.base.utils.converters.LocalTimeConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Serializable
data class Performance(
    val id : Int,
    val billNum : String,
    val billType : String? = null,
    val docType : String,
    val name : String,
    @Serializable(with = LocalDateConverter::class)
    val date : LocalDate? = null,
    @Serializable(with = LocalTimeConverter::class)
    val time: LocalTime? = null,
    val grade : String,
    val ticketNum : String? = null,
    val teacher : String,
    val course : Int,
    val semester: Int,
    val examType : String,
    val chair : String
)
