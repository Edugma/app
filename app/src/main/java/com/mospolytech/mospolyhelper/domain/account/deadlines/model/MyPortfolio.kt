package com.mospolytech.mospolyhelper.domain.account.deadlines.model

import kotlinx.serialization.Serializable

@Serializable
data class MyPortfolio(
    val otherInformation: String,
    val isPublic: Boolean = true
)