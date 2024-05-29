package com.edugma.features.account.domain.repository

import com.edugma.core.api.utils.LceFlow
import com.edugma.features.account.domain.model.performance.PerformanceDto

interface PerformanceRepository {
    fun getPerformance(
        periodId: String? = null,
        forceUpdate: Boolean = false,
    ): LceFlow<PerformanceDto>
}
