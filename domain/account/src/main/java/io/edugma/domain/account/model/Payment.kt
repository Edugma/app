package io.edugma.domain.account.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val date: LocalDate,
    val value: String,
)
