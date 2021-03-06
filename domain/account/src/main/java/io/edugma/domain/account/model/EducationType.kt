package io.edugma.domain.account.model

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
    College
}

fun EducationType.print() = when(this) {
    EducationType.Bachelor -> "Бакалавр"
    EducationType.Magistrate -> "Магистр"
    EducationType.Aspirant -> "Аспирант"
    EducationType.College -> "Студент колледжа"
}