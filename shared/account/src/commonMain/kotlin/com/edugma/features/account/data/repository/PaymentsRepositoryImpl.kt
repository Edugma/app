package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.save
import com.edugma.core.api.utils.LceFlow
import com.edugma.data.base.consts.CacheConst.PaymentsKey
import com.edugma.data.base.store.store
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.payments.PaymentsDto
import com.edugma.features.account.domain.repository.PaymentsRepository
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
