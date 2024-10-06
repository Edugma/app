package com.edugma.features.account.accounts

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.EdScaffold
import com.edugma.core.designSystem.organism.cell.EdCell
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import com.edugma.features.account.domain.model.accounts.AccountModel

@Composable
fun AccountsScreen(viewModel: AccountsViewModel = getViewModel()) {
    val state by viewModel.collectAsState()

    val onAction = viewModel.rememberOnAction()

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
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
            AccountGroupList(
                state = state,
                modifier = Modifier.fillMaxHeight(),
            )
        }
    }
}

@Composable
fun AccountGroupList(
    state: AccountsUiState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        state.accountGroups.forEachIndexed { index, accountGroup ->
            itemsIndexed(
                items = accountGroup.accounts,
                key = { _, account -> accountGroup.id + account.id },
                contentType = { _, _ -> null },
            ) { index, account ->
                AccountContent(
                    account = account,
                    isSelected = state.selectedAccountId == account.id,
                )
            }
        }
    }
}

@Composable
fun AccountContent(
    account: AccountModel,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    EdCell(
        title = account.name,
        subtitle = account.description,
        avatar = account.avatar,
        modifier = modifier,
    ) {
        if (isSelected) {
            RadioButton(
                selected = isSelected,
                onClick = null,
            )
        }
    }
}
