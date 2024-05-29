package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.save
import com.edugma.core.api.utils.LceFlow
import com.edugma.data.base.consts.CacheConst.PersonalKey
import com.edugma.data.base.store.store
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.Personal
import com.edugma.features.account.domain.repository.PersonalRepository
import kotlin.time.Duration.Companion.days

class PersonalRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PersonalRepository {

    private val store = store<Unit, Personal> {
        fetcher { _ ->
            api.getPersonalInfo()
        }
        cache {
            reader { _ ->
                cacheRepository.getFlow(PersonalKey)
            }
            writer { _, data ->
                cacheRepository.save(PersonalKey, data)
            }
            expiresIn(1.days)
        }
        coroutineScope()
    }

    override fun getPersonalInfo(forceUpdate: Boolean): LceFlow<Personal> {
        return store.get(Unit, forceUpdate = forceUpdate)
    }
}
