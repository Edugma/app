package com.edugma.features.account.domain.model.applications
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val creationDateTime: LocalDateTime,
    val number: String,
    val question: String,
    val status: String?,
    val statusDateTime: LocalDateTime?,
    val department: String?,
    val additionalInfo: String?,
)
