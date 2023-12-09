package io.edugma.features.account.domain.model.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Performance(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("description")
    val description: String,
    @SerialName("grade")
    val grade: Grade,
)

@Serializable
data class Grade(
    @SerialName("title")
    val title: String,
    @SerialName("value")
    val value: GradeValue?,
)

@Serializable
enum class GradeValue {
    @SerialName("very_good")
    VERY_GOOD,

    @SerialName("good")
    GOOD,

    @SerialName("normal")
    NORMAL,

    @SerialName("bad")
    BAD,

    @SerialName("very_bad")
    VERY_BAD,
}