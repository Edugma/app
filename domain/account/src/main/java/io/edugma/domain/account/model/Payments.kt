package io.edugma.domain.account.model

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Payments (
	val id : String,
	val student : String,
	val number : String,
	val name : String,
	val type : String,
	val level : String?,
	val dormNum : String?,
	val dormRoom : String?,
	@Serializable(with = LocalDateConverter::class)
	val startDate : LocalDate,
	@Serializable(with = LocalDateConverter::class)
	val endDate : LocalDate,
	val qr : String,
	val sum : String,
	val balance : String,
	val balanceCurrent : String,
	@Serializable(with = LocalDateConverter::class)
	val lastPaymentDate : LocalDate?,
	val payments : List<Payment>
)