package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.get
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.getOnlyData
import com.edugma.core.api.repository.save
import com.edugma.core.api.utils.UUID
import com.edugma.core.api.utils.awaitFinished
import com.edugma.data.base.consts.CacheConst
import com.edugma.data.base.store.Store
import com.edugma.data.base.store.store
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.accounts.AccountGroupModel
import com.edugma.features.account.domain.model.accounts.AccountModel
import com.edugma.features.account.domain.model.accounts.AccountsModel
import com.edugma.features.account.domain.repository.AuthorizationRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration.Companion.days

class AccountRepositoryImpl(
    private val api: AccountService,
    private val settingsRepository: SettingsRepository,
    private val cacheRepository: CacheRepository,
    private val authorizationRepository: AuthorizationRepository,
) {
    private val accountsStore: Store<String, AccountGroupModel> = store {
        fetcher { accountGroupId ->
            val accountsModel = api.getAccounts()
            val currentAccountGroup = cacheRepository.getOnlyData<AccountGroupModel>(
                CacheConst.AccountGroupKey + accountGroupId,
            )
            currentAccountGroup!!.updateWith(accountsModel)
        }
        cache {
            reader { accountGroupId ->
                cacheRepository.getFlow<AccountGroupModel>(CacheConst.AccountGroupKey + accountGroupId)
            }
            writer { accountGroupId, data ->
                updateCache(accountGroupId, data)
            }
            expiresIn(1.days)
        }
    }

    private suspend fun updateCache(
        accountGroupId: String,
        newData: AccountGroupModel,
    ) {
        val accountGroupIdList =
            settingsRepository.get<List<String>>(CacheConst.AccountGroupIdListKey)
        if (accountGroupIdList == null || accountGroupId !in accountGroupIdList) {
            settingsRepository.save(CacheConst.AccountGroupIdListKey, listOf(accountGroupId))
        }
        val previousAccountGroup = cacheRepository.getOnlyData<AccountGroupModel>(
            CacheConst.AccountGroupKey + accountGroupId,
        )
        val resData = if (previousAccountGroup != null) {
            newData.copy(
                selected = previousAccountGroup.selected,
            )
        } else {
            newData
        }
        cacheRepository.save(CacheConst.AccountGroupKey + accountGroupId, resData)
        accountGroupUpdated.tryEmit(Unit)
    }

    private val accountGroupUpdated = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    ).apply {
        tryEmit(Unit)
    }

    // TODO читать токены аккаунта и отправлять их
    suspend fun selectAccount(accountGroupId: String, accountId: String) {
        val previousAccountGroupId = settingsRepository.getString(CacheConst.SelectedAccountGroupIdKey)

        val accountGroup = cacheRepository.getOnlyData<AccountGroupModel>(CacheConst.AccountGroupKey + accountGroupId)
        checkNotNull(accountGroup)
        val selectedAccount = accountGroup.accounts.first { it.id == accountId }
        val updatedAccountGroup = accountGroup.copy(
            selected = selectedAccount.id
        )
        cacheRepository.save(CacheConst.AccountGroupKey + accountGroupId, updatedAccountGroup)
        settingsRepository.save(CacheConst.SelectedAccountKey, selectedAccount)
        settingsRepository.save(CacheConst.SelectedAccountGroupIdKey, accountGroupId)
        accountGroupUpdated.tryEmit(Unit)

        if (previousAccountGroupId != accountGroupId) {
            authorizationRepository.setCurrentToken(
                accessToken = accountGroup.accessToken,
                refreshToken = accountGroup.refreshToken,
            )
        }

        authorizationRepository.clearAccountCache()
    }

    suspend fun selectAccountGroup(accountGroupId: String) {
        val accountGroup = cacheRepository.getOnlyData<AccountGroupModel>(CacheConst.AccountGroupKey + accountGroupId)
        checkNotNull(accountGroup)
        val selectedAccountId = accountGroup.selected ?: accountGroup.default
        val selectedAccount = accountGroup.accounts.first { it.id == selectedAccountId }
        val updatedAccountGroup = accountGroup.copy(
            selected = selectedAccount.id
        )
        cacheRepository.save(CacheConst.AccountGroupKey + accountGroupId, updatedAccountGroup)
        settingsRepository.save(CacheConst.SelectedAccountKey, selectedAccount)
        settingsRepository.save(CacheConst.SelectedAccountGroupIdKey, accountGroupId)
        accountGroupUpdated.tryEmit(Unit)
    }

    fun getSelectedAccount(): Flow<AccountModel?> {
        return settingsRepository.getFlow<AccountModel>(CacheConst.SelectedAccountKey)
    }

    fun getSelectedAccountGroupId(): Flow<String?> {
        return settingsRepository.getFlow(CacheConst.SelectedAccountGroupIdKey)
    }

    suspend fun addNewAccountGroupFromToken(accessToken: String, refreshToken: String?): String {
        val newAccountGroupId = UUID.get()
        val newAccountGroup = AccountGroupModel(
            id = newAccountGroupId,
            default = null,
            selected = null,
            accessToken = accessToken,
            refreshToken = refreshToken,
            accounts = emptyList(),
        )

        updateCache(
            accountGroupId = newAccountGroupId,
            newData = newAccountGroup
        )

        return newAccountGroupId
    }

    // TODO очистка всего лишнего кэша, где есть ключ + id
    fun getAllAccountGroups(): Flow<List<AccountGroupModel>?> {
        return accountGroupUpdated.map {
            val accountGroupIdList =
                settingsRepository.get<List<String>>(CacheConst.AccountGroupIdListKey)
            accountGroupIdList?.mapNotNull { accountGroupId ->
                cacheRepository.getOnlyData<AccountGroupModel>(
                    CacheConst.AccountGroupKey + accountGroupId,
                )
            }
        }
    }

    /**
     * Only for migration from first version of app.
     */
    suspend fun createNewAccountGroupFromCurrentToken(): String {
        val accessToken = authorizationRepository.getAccessToken()!!
        val refreshToken = authorizationRepository.getRefreshToken()

        val newAccountGroupId = addNewAccountGroupFromToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
        accountsStore.get(newAccountGroupId).awaitFinished()
        return newAccountGroupId
    }

    private fun AccountGroupModel.updateWith(response: AccountsModel): AccountGroupModel {
        val selectedAccountId = response.default ?: response.accounts.firstOrNull()?.id
        return copy(
            default = response.default,
            selected = selectedAccountId,
            accounts = response.accounts,
        )
    }
}
