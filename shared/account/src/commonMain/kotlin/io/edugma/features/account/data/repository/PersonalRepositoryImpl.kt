package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getData
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.onSuccess
import io.edugma.data.base.consts.CacheConst.PersonalKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.Personal
import io.edugma.features.account.domain.repository.PersonalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class PersonalRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PersonalRepository {

    override suspend fun getPersonalInfoSuspend(): Result<Personal> {
        return api.getPersonalInfo()
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
        return cacheRepository.getData(PersonalKey)
    }
}
