package io.edugma.features.account.domain.repository

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.account.domain.model.performance.PerformanceApi

interface PerformanceRepository {
    suspend fun getPerformance(
        periodId: String? = null,
        forceUpdate: Boolean = false,
    ): LceFlow<PerformanceApi>
}
