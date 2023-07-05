package io.edugma.features.account.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EducationForm {
    @SerialName("full_time")
    FullTime,

    @SerialName("evening")
    Evening,

    @SerialName("correspondence")
    Correspondence,
}
