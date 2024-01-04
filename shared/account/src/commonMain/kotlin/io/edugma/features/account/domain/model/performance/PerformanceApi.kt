package io.edugma.features.account.domain.model.performance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceApi(
    @SerialName("periods")
    val periods: List<PerformancePeriod>,
    @SerialName("selected")
    val selected: List<Performance>,
)
