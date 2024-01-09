package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.LceFlow
import io.edugma.data.base.consts.CacheConst.PerformanceKey
import io.edugma.data.base.store.store
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.performance.PerformanceDto
import io.edugma.features.account.domain.repository.PerformanceRepository
import kotlin.time.Duration.Companion.days

class PerformanceRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PerformanceRepository {

    private val store = store<String, PerformanceDto> {
        fetcher { key ->
            api.getPerformance(key)
        }
        cache {
            reader { key ->
                cacheRepository.getFlow(PerformanceKey + key)
            }
            writer { key, data ->
                cacheRepository.save(PerformanceKey + key, data)
            }
            expiresIn(1.days)
        }
        coroutineScope()
    }

    override fun getPerformance(
        periodId: String?,
        forceUpdate: Boolean,
    ): LceFlow<PerformanceDto> {
        return store.get(periodId.orEmpty(), forceUpdate = forceUpdate)
    }
}
