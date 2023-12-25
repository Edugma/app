package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getData
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.IO
import io.edugma.data.base.consts.CacheConst.PerformanceKey
import io.edugma.data.base.consts.CacheConst.PerformancePeriodsKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.domain.model.performance.PerformancePeriod
import io.edugma.features.account.domain.repository.PerformanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PerformanceRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PerformanceRepository {
    override suspend fun getPerformancePeriods(): List<PerformancePeriod> {
        val periods = api.getPerformancePeriods()
        setLocalPeriods(periods)
        return periods
    }

    override suspend fun getPerformance(periodId: String?): List<Performance> {
        val performance = api.getPerformance(periodId.orEmpty())
        withContext(Dispatchers.IO) {
            if (periodId == null) {
                setLocalMarks(performance)
            }
        }
        return performance
    }

    override suspend fun getLocalMarks(): List<Performance>? {
        return cacheRepository.getData<List<Performance>>(PerformanceKey)
    }

    override suspend fun getLocalPerformancePeriods(): List<PerformancePeriod>? {
        return cacheRepository.getData<List<PerformancePeriod>>(PerformancePeriodsKey)
    }

    override suspend fun setLocalMarks(data: List<Performance>) {
        cacheRepository.save(PerformanceKey, data)
    }

    override suspend fun setLocalPeriods(periods: List<PerformancePeriod>) {
        cacheRepository.save(PerformancePeriodsKey, periods)
    }

    override fun getMarksLocal(): Flow<List<Performance>?> {
        return flow {
            emit(getLocalMarks())
        }
    }
}
