package com.edugma.core.api.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Coordinates(
    @SerialName("lat")
    val lat: Double,
    @SerialName("lng")
    val lng: Double,
)
