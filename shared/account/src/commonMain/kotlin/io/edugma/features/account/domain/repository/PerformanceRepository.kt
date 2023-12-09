package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.domain.model.performance.PerformancePeriod
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {
    suspend fun getPerformancePeriods(): List<PerformancePeriod>
    suspend fun getPerformance(periodId: String? = null): List<Performance>
    suspend fun getLocalMarks(): List<Performance>?
    suspend fun getLocalPerformancePeriods(): List<PerformancePeriod>?
    suspend fun setLocalMarks(data: List<Performance>)
    suspend fun setLocalPeriods(periods: List<PerformancePeriod>)
    fun getMarksLocal(): Flow<List<Performance>?>
}
