package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.deleteJsonLazy
import io.edugma.data.base.local.getSourceValue
import io.edugma.domain.account.model.Login
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.base.PrefKeys.TokenKey
import io.edugma.domain.base.utils.mapResult
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AuthorizationRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS
): AuthorizationRepository {

    override fun authorization(login: String, password: String): Flow<Result<String>> {
        return api.login(Login(login, password))
            .mapResult { it.getBearer() }
            .onSuccess { saveToken(it) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun logout() {
        localStore.deleteJsonLazy<Personal>()
        localStore.delete(TokenKey)
    }

    override suspend fun getSavedToken(): String? = localStore.getSourceValue(TokenKey)

    private suspend fun saveToken(token: String) {
        localStore.setString(token, TokenKey)
    }
}