package io.edugma.features.account.domain.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.account.domain.model.performance.PerformanceDto

interface PerformanceRepository {
    fun getPerformance(
        periodId: String? = null,
        forceUpdate: Boolean = false,
    ): LceFlow<PerformanceDto>
}
