package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.AuthInterceptorRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.get
import com.edugma.data.base.consts.CacheConst
import com.edugma.features.account.domain.model.accounts.AccountModel

class AuthInterceptorRepositoryImpl(
    private val settingsRepository: SettingsRepository,
) : AuthInterceptorRepository {

    override suspend fun getAccessToken(): String? {
        val selectedAccount = settingsRepository.get<AccountModel>(CacheConst.SelectedAccountKey)
        val accessToken = if (selectedAccount?.accessToken == null) {
            settingsRepository.getString(CacheConst.CurrentAccountGroupAccessTokenKey)
        } else {
            selectedAccount.accessToken
        }
        return accessToken
    }

    override suspend fun getRefreshToken(): String? {
        val selectedAccount = settingsRepository.get<AccountModel>(CacheConst.SelectedAccountKey)
        val refreshToken = if (selectedAccount?.refreshToken == null) {
            settingsRepository.getString(CacheConst.CurrentAccountGroupRefreshTokenKey)
        } else {
            selectedAccount.refreshToken
        }
        return refreshToken
    }

    override suspend fun getAccountId(): String? {
        val selectedAccount = settingsRepository.get<AccountModel>(CacheConst.SelectedAccountKey)

        return selectedAccount?.id
    }
}
