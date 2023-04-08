package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.ApplicationsKey
import io.edugma.data.base.consts.CacheConst.ClassmatesKey
import io.edugma.data.base.consts.CacheConst.CourseKey
import io.edugma.data.base.consts.CacheConst.PaymentsKey
import io.edugma.data.base.consts.CacheConst.PerformanceKey
import io.edugma.data.base.consts.CacheConst.PersonalKey
import io.edugma.data.base.consts.CacheConst.SemesterKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getSourceValue
import io.edugma.domain.account.model.Login
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.base.PrefKeys.TokenKey
import io.edugma.domain.base.utils.mapResult
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthorizationRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS,
) : AuthorizationRepository {

    override fun authorization(login: String, password: String): Flow<Result<String>> {
        return api.login(Login(login, password))
            .mapResult { it.getBearer() }
            .onSuccess(::saveToken)
            .flowOn(Dispatchers.IO)
    }

    override suspend fun authorizationSuspend(login: String, password: String): Result<String> {
        return api.loginSuspend(Login(login, password))
            .map { it.getBearer() }
            .onSuccess {
                withContext(Dispatchers.IO) {
                    saveToken(it)
                }
            }
    }

    override suspend fun logout() {
        localStore.delete(PersonalKey)
        localStore.delete(ApplicationsKey)
        localStore.delete(PerformanceKey)
        localStore.delete(CourseKey)
        localStore.delete(SemesterKey)
        localStore.delete(PaymentsKey)
        localStore.delete(ClassmatesKey)
        localStore.delete(TokenKey)
    }

    override suspend fun getSavedToken(): String? = localStore.getSourceValue(TokenKey)

    private suspend fun saveToken(token: String) {
        localStore.setString(token, TokenKey)
    }
}
