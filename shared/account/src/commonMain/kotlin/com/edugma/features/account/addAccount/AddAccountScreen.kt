package com.edugma.features.account.addAccount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.textField.EdTextField
import com.edugma.core.designSystem.organism.EdScaffold
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel
import edugma.shared.core.icons.generated.resources.ic_fluent_eye_16_regular
import edugma.shared.core.icons.generated.resources.ic_fluent_eye_off_16_regular
import org.jetbrains.compose.resources.painterResource

@Composable
fun AddAccountScreen(viewModel: AddAccountViewModel = getViewModel()) {
    val state by viewModel.collectAsState()

    val onAction = viewModel.rememberOnAction()

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = false,
    ) {
        AddAccountContent(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun AddAccountContent(
    state: AddAccountUiState,
    onAction: (AddAccountAction) -> Unit,
) {
    EdScaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            EdTopAppBar(
                title = "Добавление нового аккаунта",
                onNavigationClick = {
                    onAction(AddAccountAction.OnBack)
                },
                windowInsets = WindowInsets.statusBars,
                actions = {},
            )
        },
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.top(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                EdTextField(
                    value = state.login,
                    placeholder = "Логин",
                    onValueChange = {
                        onAction(AddAccountAction.LoginEnter(it))
                    },
                    isError = state.loginError,
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(0.9f),
                )
                SpacerHeight(height = 12.dp)
                EdTextField(
                    value = state.password,
                    placeholder = "Пароль",
                    onValueChange = {
                        onAction(AddAccountAction.PasswordEnter(it))
                    },
                    isError = state.passwordError,
                    visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (state.passwordVisible) {
                            IconButton(
                                onClick = {
                                    onAction(AddAccountAction.SetPasswordVisible(false))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(EdIcons.ic_fluent_eye_off_16_regular),
                                    contentDescription = null,
                                )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    onAction(AddAccountAction.SetPasswordVisible(true))
                                }
                            ) {
                                Icon(
                                    painter = painterResource(EdIcons.ic_fluent_eye_16_regular),
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(0.9f),
                )
                SpacerHeight(height = 2.dp)
                EdLabel(
                    text = state.error,
                    color = EdTheme.colorScheme.error,
                    style = EdTheme.typography.bodySmall,
                    modifier = Modifier
                        .widthIn(max = 500.dp)
                        .fillMaxWidth(0.9f),
                )
                SpacerHeight(height = 15.dp)
                EdButton(
                    modifier = Modifier
                        .widthIn(250.dp, 450.dp)
                        .fillMaxWidth(0.8f),
                    onClick = {
                        onAction(AddAccountAction.OnLoginClick)
                    },
                    isLoading = state.isLoading,
                    text = "Войти",
                )
            }
        }
    }
}
