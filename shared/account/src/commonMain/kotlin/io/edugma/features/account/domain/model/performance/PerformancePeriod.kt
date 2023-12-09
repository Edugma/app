package io.edugma.features.account.domain.model.performance
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformancePeriod(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
)
