package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.LceFlow
import io.edugma.data.base.consts.CacheConst.PaymentsKey
import io.edugma.data.base.store.store
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.payments.PaymentsDto
import io.edugma.features.account.domain.repository.PaymentsRepository
import kotlin.time.Duration.Companion.days

class PaymentsRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PaymentsRepository {

    private val store = store<String, PaymentsDto> {
        fetcher { key ->
            api.getPayments(key)
        }
        cache {
            reader { key ->
                cacheRepository.getFlow(PaymentsKey + key)
            }
            writer { key, data ->
                cacheRepository.save(PaymentsKey + key, data)
            }
            expiresIn(1.days)
        }
        coroutineScope()
    }

    override fun getPayments(
        contractId: String?,
        forceUpdate: Boolean,
    ): LceFlow<PaymentsDto> {
        return store.get(key = contractId.orEmpty(), forceUpdate = forceUpdate)
    }
}
