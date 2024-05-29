package com.edugma.features.account.menu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.textField.EdTextField
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.features.account.menu.MenuState

@Composable
fun AuthorizationScreen(
    state: MenuState.Authorization,
    onLoginClick: ClickListener,
    onPasswordChange: Typed1Listener<String>,
    onLoginChange: Typed1Listener<String>,
    setPasswordVisible: ClickListener,
    setPasswordInvisible: ClickListener,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 20.dp, start = 4.dp, end = 4.dp),
    ) {
        Text(
            text = "Сервисы",
            style = EdTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp),
        )
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
                onValueChange = onLoginChange,
                isError = state.loginError,
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.9f),
            )
            SpacerHeight(height = 12.dp)
            EdTextField(
                value = state.password,
                placeholder = "Пароль",
                onValueChange = onPasswordChange,
                isError = state.passwordError,
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (state.passwordVisible) {
                        IconButton(setPasswordInvisible) {
                            Icon(
                                painter = painterResource(EdIcons.ic_fluent_eye_off_16_regular),
                                contentDescription = null,
                            )
                        }
                    } else {
                        IconButton(setPasswordVisible) {
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
                onClick = onLoginClick,
                isLoading = state.isLoading,
                text = "Войти",
            )
        }
    }
}
