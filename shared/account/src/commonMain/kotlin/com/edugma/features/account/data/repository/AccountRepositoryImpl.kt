package com.edugma.features.account.data.repository

import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.get
import com.edugma.core.api.repository.getFlow
import com.edugma.core.api.repository.getOnlyData
import com.edugma.core.api.repository.save
import com.edugma.core.api.utils.UUID
import com.edugma.core.api.utils.UpdateFlow
import com.edugma.core.api.utils.awaitFinished
import com.edugma.data.base.consts.CacheConst.AccountGroupIdListKey
import com.edugma.data.base.consts.CacheConst.AccountGroupKey
import com.edugma.data.base.consts.CacheConst.SelectedAccountGroupIdKey
import com.edugma.data.base.consts.CacheConst.SelectedAccountKey
import com.edugma.data.base.store.Store
import com.edugma.data.base.store.store
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.accounts.AccountGroupModel
import com.edugma.features.account.domain.model.accounts.AccountModel
import com.edugma.features.account.domain.model.accounts.AccountsModel
import com.edugma.features.account.domain.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
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
                AccountGroupKey + accountGroupId,
            )
            currentAccountGroup!!.updateWith(accountsModel)
        }
        cache {
            reader { accountGroupId ->
                cacheRepository.getFlow<AccountGroupModel>(AccountGroupKey + accountGroupId)
            }
            writer { accountGroupId, data ->
                updateCache(accountGroupId, data, true)
            }
            expiresIn(1.days)
        }
    }

    private suspend fun updateCache(
        accountGroupId: String,
        newData: AccountGroupModel,
        updateTimestamp: Boolean,
    ) {
        val accountGroupIdList =
            settingsRepository.get<List<String>>(AccountGroupIdListKey)
        if (accountGroupIdList == null) {
            settingsRepository.save(AccountGroupIdListKey, listOf(accountGroupId))
        } else if (accountGroupId !in accountGroupIdList) {
            settingsRepository.save(AccountGroupIdListKey, accountGroupIdList + accountGroupId)
        }
        val previousAccountGroup = cacheRepository.getOnlyData<AccountGroupModel>(
            AccountGroupKey + accountGroupId,
        )
        val resData = if (previousAccountGroup?.selected != null) {
            newData.copy(
                selected = previousAccountGroup.selected,
            )
        } else {
            newData
        }
        cacheRepository.save(
            AccountGroupKey + accountGroupId,
            resData,
            updateTimestamp = updateTimestamp,
        )
        accountGroupUpdated.updated()
    }

    private val accountGroupUpdated = UpdateFlow()

    suspend fun selectAccount(accountGroupId: String, accountId: String) {
        val previousAccountGroupId = settingsRepository.getString(SelectedAccountGroupIdKey)

        val accountGroup = cacheRepository.getOnlyData<AccountGroupModel>(AccountGroupKey + accountGroupId)
        checkNotNull(accountGroup)
        val selectedAccount = accountGroup.accounts.first { it.id == accountId }
        val updatedAccountGroup = accountGroup.copy(
            selected = selectedAccount.id
        )
        cacheRepository.save(AccountGroupKey + accountGroupId, updatedAccountGroup)
        settingsRepository.save(SelectedAccountKey, selectedAccount)
        settingsRepository.saveString(SelectedAccountGroupIdKey, accountGroupId)

        if (previousAccountGroupId != accountGroupId) {
            authorizationRepository.setCurrentToken(
                accessToken = accountGroup.accessToken,
                refreshToken = accountGroup.refreshToken,
            )
        }

        authorizationRepository.clearAccountCache()

        accountGroupUpdated.updated()
    }

    fun getSelectedAccount(): Flow<AccountModel?> {
        return settingsRepository.getFlow<AccountModel>(SelectedAccountKey)
    }

    fun getSelectedAccountGroupId(): Flow<String?> {
        return settingsRepository.getStringFlow(SelectedAccountGroupIdKey)
    }

    private suspend fun addNewEmptyAccountGroupFromToken(
        accessToken: String,
        refreshToken: String?,
    ): String {
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
            newData = newAccountGroup,
            updateTimestamp = false,
        )

        return newAccountGroupId
    }

    fun getAllAccountGroups(): Flow<List<AccountGroupModel>?> {
        return accountGroupUpdated.map {
            val accountGroupIdList =
                settingsRepository.get<List<String>>(AccountGroupIdListKey)
            accountGroupIdList?.mapNotNull { accountGroupId ->
                cacheRepository.getOnlyData<AccountGroupModel>(
                    AccountGroupKey + accountGroupId,
                )
            }
        }
    }

    suspend fun selectAccountGroup(accountGroupId: String) {
        val accountGroup = cacheRepository.getOnlyData<AccountGroupModel>(AccountGroupKey + accountGroupId)
        checkNotNull(accountGroup)
        val selectedAccountId = accountGroup.selected ?: accountGroup.default
        val selectedAccount = accountGroup.accounts.first { it.id == selectedAccountId }
        val updatedAccountGroup = accountGroup.copy(
            selected = selectedAccount.id
        )
        cacheRepository.save(AccountGroupKey + accountGroupId, updatedAccountGroup)
        settingsRepository.save(SelectedAccountKey, selectedAccount)
        settingsRepository.saveString(SelectedAccountGroupIdKey, accountGroupId)
        accountGroupUpdated.updated()
    }

    suspend fun clearSelectedAccount() {
        settingsRepository.removeObject(SelectedAccountKey)
    }

    suspend fun createNewAccountGroupFromToken(
        accessToken: String,
        refreshToken: String?,
    ): String {
        val newAccountGroupId = addNewEmptyAccountGroupFromToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
        accountsStore.get(newAccountGroupId).awaitFinished()
        return newAccountGroupId
    }

    /**
     * Only for migration from first version of app.
     */
    suspend fun createNewAccountGroupFromCurrentToken(): String? {
        val accessToken = authorizationRepository.getAccessToken() ?: return null
        return createNewAccountGroupFromToken(
            accessToken = accessToken,
            refreshToken = authorizationRepository.getRefreshToken()
        )
    }

    private fun AccountGroupModel.updateWith(response: AccountsModel): AccountGroupModel {
        val selectedAccountId = response.default ?: response.accounts.firstOrNull()?.id
        return copy(
            default = response.default,
            selected = selectedAccountId,
            accounts = response.accounts,
        )
    }

    suspend fun deleteAccountGroup(accountGroupId: String) {
        val accountGroupIdList =
            settingsRepository.get<List<String>>(AccountGroupIdListKey)
        if (accountGroupIdList != null) {
            settingsRepository.save(AccountGroupIdListKey, accountGroupIdList - accountGroupId)
        }
        cacheRepository.remove(AccountGroupKey + accountGroupId)

        val selectedAccountGroupId = settingsRepository.getString(SelectedAccountGroupIdKey)

        if (selectedAccountGroupId == selectedAccountGroupId) {
            val newAccountGroupIdList =
                settingsRepository.get<List<String>>(AccountGroupIdListKey)
            if (newAccountGroupIdList.isNullOrEmpty()) {
                authorizationRepository.logout()
            } else {
                val newSelectedAccountGroupId = newAccountGroupIdList.first()
                val accountGroup = cacheRepository.getOnlyData<AccountGroupModel>(
                    AccountGroupKey + newSelectedAccountGroupId,
                )
                checkNotNull(accountGroup)
                authorizationRepository.setCurrentToken(
                    accessToken = accountGroup.accessToken,
                    refreshToken = accountGroup.refreshToken,
                )
                selectAccountGroup(newSelectedAccountGroupId)
                authorizationRepository.clearAccountCache()
            }
        }

        accountGroupUpdated.updated()
    }
}
