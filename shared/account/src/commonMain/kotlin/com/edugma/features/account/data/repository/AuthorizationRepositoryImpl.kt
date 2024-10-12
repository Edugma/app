package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.data.base.consts.CacheConst.AccountGroupIdListKey
import com.edugma.data.base.consts.CacheConst.AccountGroupKey
import com.edugma.data.base.consts.CacheConst.ApplicationsKey
import com.edugma.data.base.consts.CacheConst.ClassmatesKey
import com.edugma.data.base.consts.CacheConst.CourseKey
import com.edugma.data.base.consts.CacheConst.CurrentAccountGroupAccessTokenKey
import com.edugma.data.base.consts.CacheConst.CurrentAccountGroupRefreshTokenKey
import com.edugma.data.base.consts.CacheConst.LkTokenKey
import com.edugma.data.base.consts.CacheConst.PaymentsKey
import com.edugma.data.base.consts.CacheConst.PerformanceKey
import com.edugma.data.base.consts.CacheConst.PersonalKey
import com.edugma.data.base.consts.CacheConst.SelectedAccountGroupIdKey
import com.edugma.data.base.consts.CacheConst.SelectedAccountKey
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.auth.Login
import com.edugma.features.account.domain.model.auth.Token
import com.edugma.features.account.domain.repository.AuthorizationRepository

class AuthorizationRepositoryImpl(
    private val api: AccountService,
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
) : AuthorizationRepository {

    override suspend fun authorize(login: String, password: String): Token {
        val token = api.login(Login(login, password)).getOrThrow()
        setCurrentToken(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        return token
    }

    override suspend fun setCurrentToken(accessToken: String, refreshToken: String?) {
        settingsRepository.saveString(CurrentAccountGroupAccessTokenKey, accessToken)
        refreshToken?.let {
            settingsRepository.saveString(CurrentAccountGroupRefreshTokenKey, refreshToken)
        }
    }

    override suspend fun logout() {
        cacheRepository.removeWithPrefix(AccountGroupKey)

        settingsRepository.removeString(CurrentAccountGroupAccessTokenKey)
        settingsRepository.removeString(CurrentAccountGroupRefreshTokenKey)
        settingsRepository.removeObject(AccountGroupIdListKey)
        settingsRepository.removeString(SelectedAccountGroupIdKey)
        settingsRepository.removeObject(SelectedAccountKey)
        clearAccountCache()
    }

    override suspend fun clearAccountCache() {
        cacheRepository.remove(PersonalKey)
        cacheRepository.remove(ApplicationsKey)
        cacheRepository.remove(PerformanceKey)
        cacheRepository.remove(CourseKey)
        cacheRepository.remove(PaymentsKey)
        cacheRepository.remove(ClassmatesKey)
    }

    override suspend fun getLkToken(): Result<String> {
        val local = getSavedLkToken()?.let { Result.success(it) }
        val remote = api.getLkToken().map { it.accessToken }.onSuccess { saveLkToken(it) }
        return local ?: remote
    }

    override suspend fun getAccessToken(): String? {
        return settingsRepository.getString(CurrentAccountGroupAccessTokenKey)
    }

    override suspend fun getRefreshToken(): String? {
        return settingsRepository.getString(CurrentAccountGroupRefreshTokenKey)
    }

    private suspend fun saveLkToken(token: String) {
        settingsRepository.saveString(LkTokenKey, token)
    }

    private suspend fun getSavedLkToken(): String? {
        return settingsRepository.getString(LkTokenKey)
    }
}
