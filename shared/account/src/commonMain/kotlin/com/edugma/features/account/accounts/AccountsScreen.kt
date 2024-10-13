package com.edugma.features.account.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import edugma.shared.core.icons.generated.resources.ic_fluent_delete_24_filled
import org.jetbrains.compose.resources.painterResource

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
                    onDeleteAccountGroup = {
                        onAction(
                            AccountsAction.DeleteAccountGroup(
                                accountGroupId = it,
                            ),
                        )
                    }
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
    onDeleteAccountGroup: (String) -> Unit,
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
                    isSelected = state.selectedAccountGroupId == accountGroup.id,
                    index = index,
                    onDelete = onDeleteAccountGroup,
                )
            }
            itemsIndexed(
                items = accountGroup.accounts,
                key = { _, account ->
                    accountGroup.id + account.id
                },
                contentType = { _, _ -> null },
            ) { index, account ->
                AccountContent(
                    account = account,
                    isSelected = accountGroup.selected == account.id,
                    isAccountGroupSelected = state.selectedAccountGroupId == accountGroup.id,
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
    accountGroup: AccountGroupUiModel,
    isSelected: Boolean,
    index: Int,
    onDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isSelected) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        EdLabel(
            text = "Группа аккаунтов ${index + 1}",
            style = EdTheme.typography.titleMedium,
            modifier = modifier.padding(end = 4.dp, top = 4.dp, bottom = 4.dp).weight(1f)
        )

        IconButton(
            onClick = {
                onDelete(accountGroup.id)
            },
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(EdIcons.ic_fluent_delete_24_filled),
                contentDescription = null,
                tint = LocalContentColor.current,
            )
        }
    }
}

@Composable
private fun AccountContent(
    account: AccountUiModel,
    isSelected: Boolean,
    isAccountGroupSelected: Boolean,
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
                enabled = isAccountGroupSelected,
                onClick = null,
            )
        }
    }
}
