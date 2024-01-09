package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.LceFlow
import io.edugma.data.base.consts.CacheConst.PersonalKey
import io.edugma.data.base.store.store
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.Personal
import io.edugma.features.account.domain.repository.PersonalRepository
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
