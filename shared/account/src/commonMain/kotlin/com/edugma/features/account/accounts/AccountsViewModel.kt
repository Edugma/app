package com.edugma.features.account.accounts

import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.navigation.AccountScreens
import com.edugma.features.account.data.repository.AccountRepositoryImpl
import com.edugma.features.account.domain.model.accounts.AccountGroupModel
import kotlinx.coroutines.flow.collectIndexed

class AccountsViewModel(
    private val accountRepository: AccountRepositoryImpl,
) : FeatureLogic<AccountsUiState, AccountsAction>() {
    override fun initialState(): AccountsUiState {
        return AccountsUiState()
    }

    override fun onCreate() {
        launchCoroutine {
            // TODO тест
//            accountRepository.clearAccountGroupDataTest()
//            val newAccountGroupId =
//                accountRepository.createNewAccountGroupFromCurrentToken()
//            accountRepository.selectAccountGroup(newAccountGroupId)
            accountRepository.getAllAccountGroups().collectIndexed { index, value ->
                legacyMigration(index, value)
                newState {
                    toContent(value?.map { it.toUiModel() } ?: emptyList())
                }
            }
        }

        launchCoroutine {
            accountRepository.getSelectedAccountGroupId().collect {
                newState {
                    copy(selectedAccountGroupId = it)
                }
            }
        }

        launchCoroutine {
            accountRepository.getSelectedAccount().collect {
                newState {
                    copy(selectedAccountId = it?.id)
                }
            }
        }
    }

    private suspend fun legacyMigration(
        index: Int,
        groups: List<AccountGroupModel>?,
    ) {
        // only for first time and only if list empty
        if (index == 0 && groups.isNullOrEmpty()) {
            accountRepository.clearSelectedAccount()
            val newAccountGroupId = accountRepository.createNewAccountGroupFromCurrentToken()
            if (newAccountGroupId != null) {
                accountRepository.selectAccountGroup(newAccountGroupId)
            }
        }
    }

    override fun processAction(action: AccountsAction) {
        when (action) {
            AccountsAction.OnBack -> accountRouter.back()

            AccountsAction.AddNewGroup -> {
                accountRouter.navigateTo(AccountScreens.AddAccount())
            }

            is AccountsAction.DeleteAccountGroup -> deleteAccountGroup(action.accountGroupId)
            is AccountsAction.SelectAccount -> selectAccount(action)
        }
    }

    private fun selectAccount(action: AccountsAction.SelectAccount) {
        launchCoroutine {
            accountRepository.selectAccount(
                accountGroupId = action.accountGroupId,
                accountId = action.accountId,
            )
        }
    }

    private fun deleteAccountGroup(accountGroupId: String) {
        launchCoroutine {
            accountRepository.deleteAccountGroup(
                accountGroupId = accountGroupId,
            )
        }
    }
}
