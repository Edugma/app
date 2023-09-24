package io.edugma.features.misc.other.inAppUpdate.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Version(
    @SerialName("min")
    val min: String,
    @SerialName("last")
    val last: String,
)
