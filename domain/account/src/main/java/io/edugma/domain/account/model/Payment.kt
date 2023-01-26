package io.edugma.domain.account.model

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Payment(
    @Serializable(LocalDateConverter::class)
    val date: LocalDate,
    val value: String,
)
