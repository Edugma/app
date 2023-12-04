package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.SettingsRepository
import io.edugma.core.api.utils.mapResult
import io.edugma.core.api.utils.onSuccess
import io.edugma.data.base.consts.CacheConst.ApplicationsKey
import io.edugma.data.base.consts.CacheConst.ClassmatesKey
import io.edugma.data.base.consts.CacheConst.CourseKey
import io.edugma.data.base.consts.CacheConst.LkTokenKey
import io.edugma.data.base.consts.CacheConst.PaymentsKey
import io.edugma.data.base.consts.CacheConst.PerformanceKey
import io.edugma.data.base.consts.CacheConst.PersonalKey
import io.edugma.data.base.consts.CacheConst.SemesterKey
import io.edugma.data.base.consts.CacheConst.TokenKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.auth.Login
import io.edugma.features.account.domain.repository.AuthorizationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class AuthorizationRepositoryImpl(
    private val api: AccountService,
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
) : AuthorizationRepository {

    override fun authorization(login: String, password: String): Flow<Result<String>> {
        return flow { emit(api.login(Login(login, password))) }
            .mapResult { it.getBearer() }
            .onSuccess(::saveToken)
            .flowOn(Dispatchers.IO)
    }

    override suspend fun authorizationSuspend(login: String, password: String): Result<String> {
        return api.login(Login(login, password))
            .map { it.getBearer() }
            .onSuccess {
                withContext(Dispatchers.IO) {
                    saveToken(it)
                }
            }
    }

    override suspend fun logout() {
        cacheRepository.remove(PersonalKey)
        cacheRepository.remove(ApplicationsKey)
        cacheRepository.remove(PerformanceKey)
        cacheRepository.remove(CourseKey)
        cacheRepository.remove(SemesterKey)
        cacheRepository.remove(PaymentsKey)
        cacheRepository.remove(ClassmatesKey)

        settingsRepository.removeString(TokenKey)
    }

    override suspend fun getLkToken(): Result<String> {
        val local = getSavedLkToken()?.let { Result.success(it) }
        val remote = api.getLkToken().map { it.token }.onSuccess { saveLkToken(it) }
        return local ?: remote
    }

    override suspend fun getSavedToken(): String? {
        return settingsRepository.getString(TokenKey)
    }

    private suspend fun saveToken(token: String) {
        settingsRepository.saveString(TokenKey, token)
    }
    private suspend fun saveLkToken(token: String) {
        settingsRepository.saveString(LkTokenKey, token)
    }

    private suspend fun getSavedLkToken(): String? {
        return settingsRepository.getString(LkTokenKey)
    }
}
