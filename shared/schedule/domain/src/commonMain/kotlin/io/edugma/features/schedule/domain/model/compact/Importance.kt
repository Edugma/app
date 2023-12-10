package io.edugma.features.schedule.domain.model.compact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Importance {
    @SerialName("low")
    Low,

    @SerialName("normal")
    Normal,

    @SerialName("high")
    High,
}
