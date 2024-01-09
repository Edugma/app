package io.edugma.features.account.domain.model.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceDto(
    @SerialName("periods")
    val periods: List<PerformancePeriod>,
    @SerialName("selected")
    val selected: Performance?,
)
