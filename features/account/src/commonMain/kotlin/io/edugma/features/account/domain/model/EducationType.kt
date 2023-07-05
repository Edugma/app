package io.edugma.features.account.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EducationType {
    @SerialName("bachelor")
    Bachelor,

    @SerialName("magistrate")
    Magistrate,

    @SerialName("aspirant")
    Aspirant,

    @SerialName("college")
    College,
}

fun io.edugma.features.account.domain.model.EducationType.print() = when (this) {
    io.edugma.features.account.domain.model.EducationType.Bachelor -> "Бакалавр"
    io.edugma.features.account.domain.model.EducationType.Magistrate -> "Магистр"
    io.edugma.features.account.domain.model.EducationType.Aspirant -> "Аспирант"
    io.edugma.features.account.domain.model.EducationType.College -> "Студент колледжа"
}
