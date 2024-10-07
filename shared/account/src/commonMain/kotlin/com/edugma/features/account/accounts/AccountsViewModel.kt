package com.edugma.features.account.accounts

import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.navigation.AccountScreens
import com.edugma.features.account.data.repository.AccountRepositoryImpl
import kotlinx.coroutines.flow.collectIndexed

class AccountsViewModel(
    private val accountRepository: AccountRepositoryImpl,
) : FeatureLogic<AccountsUiState, AccountsAction>() {
    override fun initialState(): AccountsUiState {
        return AccountsUiState()
    }

    override fun onCreate() {
        launchCoroutine {
            accountRepository.getAllAccountGroups().collectIndexed { index, value ->
                if (index == 0 && value == null) {
                    val newAccountGroupId =
                        accountRepository.createNewAccountGroupFromCurrentToken()
                    accountRepository.selectAccountGroup(newAccountGroupId)
                }
                newState {
                    copy(accountGroups = value ?: emptyList())
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

    override fun processAction(action: AccountsAction) {
        when (action) {
            AccountsAction.OnBack -> accountRouter.back()
            // TODO проверять текущую группу на актуальность

            // TODO если текущей группы нет, то создать. Если нет текущего аккаунта, то обновить
            AccountsAction.AddNewGroup -> {
                accountRouter.navigateTo(AccountScreens.AddAccount())
            }

            is AccountsAction.DeleteAccountGroup -> deleteAccountGroup(action.accountGroupId)
            is AccountsAction.SelectAccount -> selectAccount(action)
        }
    }

    private fun selectAccount(action: AccountsAction.SelectAccount) {
    }

    private fun deleteAccountGroup(accountGroupId: String) {
    }
}
