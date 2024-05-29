package com.edugma.features.account.data.repository

import co.touchlab.kermit.Logger
import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.save
import com.edugma.core.api.utils.LceFlow
import com.edugma.core.api.utils.mapLce
import com.edugma.data.base.consts.CacheConst.PerformanceKey
import com.edugma.data.base.store.store
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.performance.PerformanceDto
import com.edugma.features.account.domain.repository.PerformanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration.Companion.days

class PerformanceRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PerformanceRepository {

    // TODO Возможно стоит сохранять период отдельно
    private val lastDefaultPeriod = MutableStateFlow(DEFAULT_PERIOD)

    private val store = store<String, PerformanceDto> {
        fetcher { key ->
            api.getPerformance(key)
        }
        cache {
            reader { key ->
                cacheRepository.getFlow<PerformanceDto>(PerformanceKey + key)
                    .onEach {
                        it?.data?.selected?.id?.let { selectedPeriod ->
                            lastDefaultPeriod.value = selectedPeriod
                        }
                    }
            }
            writer { key, data ->
                cacheRepository.save(PerformanceKey + key, data)
                if (key == DEFAULT_PERIOD) {
                    if (data.selected != null) {
                        cacheRepository.save(PerformanceKey + data.selected.id, data)
                        lastDefaultPeriod.value = data.selected.id
                    }
                } else if (key == lastDefaultPeriod.value) {
                    cacheRepository.save(PerformanceKey + DEFAULT_PERIOD, data)
                }
            }
            expiresIn(1.days)
        }
        coroutineScope()
    }

    override fun getPerformance(
        periodId: String?,
        forceUpdate: Boolean,
    ): LceFlow<PerformanceDto> {
        return store.get(periodId ?: DEFAULT_PERIOD, forceUpdate = forceUpdate)
    }

    companion object {
        private const val DEFAULT_PERIOD = ""
    }
}
