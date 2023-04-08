package io.edugma.features.account.menu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.textField.EdTextField
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.account.menu.MenuState
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener

@Composable
fun AuthorizationScreen(
    state: MenuState.Authorization,
    onLoginClick: ClickListener,
    onPasswordChange: Typed1Listener<String>,
    onLoginChange: Typed1Listener<String>,
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
                .padding(horizontal = 32.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            EdTextField(
                value = state.login,
                placeholder = "Логин",
                onValueChange = onLoginChange,
            )
            SpacerHeight(height = 12.dp)
            EdTextField(
                value = state.password,
                placeholder = "Пароль",
                onValueChange = onPasswordChange,
                visualTransformation = PasswordVisualTransformation(),
            )
            SpacerHeight(height = 25.dp)
            EdButton(
                modifier = Modifier.defaultMinSize(minWidth = 250.dp),
                onClick = onLoginClick,
                isLoading = state.isLoading,
                text = "Войти",
            )
        }
    }
}
