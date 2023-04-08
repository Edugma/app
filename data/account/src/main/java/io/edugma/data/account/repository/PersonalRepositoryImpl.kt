package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.PersonalKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getJsonLazy
import io.edugma.data.base.local.setJsonLazy
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.PersonalRepository
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PersonalRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS,
) : PersonalRepository {
    override fun getPersonalInfo() =
        api.getPersonalInfo()
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
        localStore.setJsonLazy(personal, PersonalKey)
    }

    override suspend fun getLocalPersonalInfo(): Personal? = localStore.getJsonLazy(PersonalKey)
}
