package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.PersonalKey
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.get
import io.edugma.domain.base.repository.save
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PersonalRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PersonalRepository {
    override fun getPersonalInfo() =
        flow { emit(api.getPersonalInfo()) }
            .onSuccess { setLocalPersonalInfo(it) }
            .flowOn(Dispatchers.IO)

    override suspend fun getPersonalInfoSuspend(): Result<Personal> {
        return api.getPersonalInfoSuspend()
            .onSuccess {
                withContext(Dispatchers.IO) {
                    setLocalPersonalInfo(it)
                }
            }
    }

    override suspend fun setLocalPersonalInfo(personal: Personal) {
        cacheRepository.save(PersonalKey, personal)
    }

    override suspend fun getLocalPersonalInfo(): Personal? {
        return cacheRepository.get(PersonalKey)
    }
}
