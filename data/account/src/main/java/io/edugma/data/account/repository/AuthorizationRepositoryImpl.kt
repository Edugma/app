package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.local.PreferencesDS
import io.edugma.domain.account.model.Login
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.base.PrefKeys.TokenKey
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class AuthorizationRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS
): AuthorizationRepository {

    override fun authorization(login: String, password: String): Flow<Result<String>> {
        return api.login(Login(login, password))
            .map { result -> result.map { it.getBearer() } }
            .onSuccess { saveToken(it) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getSavedToken(): String? = localStore.getString(TokenKey).getOrNull()?.value

    private suspend fun saveToken(token: String) {
        localStore.setString(token, TokenKey)
    }
}