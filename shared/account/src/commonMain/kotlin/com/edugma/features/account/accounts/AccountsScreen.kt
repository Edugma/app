package com.edugma.features.account.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.organism.EdScaffold
import com.edugma.core.designSystem.organism.cell.EdCell
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.account.domain.model.accounts.AccountGroupModel
import com.edugma.features.account.domain.model.accounts.AccountModel

@Composable
fun AccountsScreen(viewModel: AccountsViewModel = getViewModel()) {
    val state by viewModel.collectAsState()

    val onAction = viewModel.rememberOnAction()

    FeatureScreen(
        statusBarPadding = false,
    ) {
        AccountsContent(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun AccountsContent(
    state: AccountsUiState,
    onAction: (AccountsAction) -> Unit,
) {
    EdScaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            EdTopAppBar(
                title = "Аккаунты",
                onNavigationClick = {
                    onAction(AccountsAction.OnBack)
                },
                windowInsets = WindowInsets.statusBars,
                actions = {},
            )
        },
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.top(),
        ) {
            Column(Modifier.fillMaxSize()) {
                AccountGroupList(
                    state = state,
                    modifier = Modifier.weight(1f),
                    onAccountClick = { accountGroupId, accountId ->
                        onAction(
                            AccountsAction.SelectAccount(
                                accountGroupId = accountGroupId,
                                accountId = accountId,
                            ),
                        )
                    },
                )
                EdButton(
                    text = "Добавить",
                    onClick = {
                        onAction(AccountsAction.AddNewGroup)
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 5.dp,
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun AccountGroupList(
    state: AccountsUiState,
    onAccountClick: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        state.accountGroups.forEachIndexed { index, accountGroup ->
            item(
                key = accountGroup.id,
                contentType = null,
            ) {
                AccountGroupTitle(
                    accountGroup = accountGroup,
                    index = index,
                )
            }
            itemsIndexed(
                items = accountGroup.accounts,
                key = { _, account -> accountGroup.id + account.id },
                contentType = { _, _ -> null },
            ) { index, account ->
                AccountContent(
                    account = account,
                    isSelected = state.selectedAccountId == account.id,
                    onClick = {
                        onAccountClick(accountGroup.id, account.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun AccountGroupTitle(
    accountGroup: AccountGroupModel,
    index: Int,
    modifier: Modifier = Modifier,
) {
    EdLabel(
        text = "Группа аккаунтов ${index + 1}",
        style = EdTheme.typography.titleMedium,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    )
}

@Composable
private fun AccountContent(
    account: AccountModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EdCell(
        title = account.name,
        subtitle = account.description,
        avatar = account.avatar,
        modifier = modifier,
        onClick = onClick,
    ) {
        if (isSelected) {
            RadioButton(
                selected = isSelected,
                onClick = null,
            )
        }
    }
}
