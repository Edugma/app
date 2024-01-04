package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getDataFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.LceFlow
import io.edugma.data.base.consts.CacheConst.PerformanceKey
import io.edugma.data.base.store.store
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.performance.PerformanceApi
import io.edugma.features.account.domain.repository.PerformanceRepository
import kotlin.time.Duration.Companion.days

class PerformanceRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PerformanceRepository {

    // TODO при ошибке парсинга не ронять всё, а ждать новые данные
    private val performanceStore = store<String, PerformanceApi> {
        fetcher { key ->
            api.getPerformance(key)
        }
        cache {
            reader { key ->
                cacheRepository.getDataFlow(PerformanceKey + key)
            }
            writer { key, data ->
                cacheRepository.save(PerformanceKey + key, data)
            }
            expiresIn(1.days)
        }
        coroutineScope()
    }

    override suspend fun getPerformance(
        periodId: String?,
        forceUpdate: Boolean,
    ): LceFlow<PerformanceApi> {
        return performanceStore.get(periodId.orEmpty(), forceUpdate = forceUpdate)
    }
}
