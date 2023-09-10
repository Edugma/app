package io.edugma.features.account.menu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.features.account.menu.screens.AuthorizationScreen
import io.edugma.features.account.menu.screens.LoadingScreen
import io.edugma.features.account.menu.screens.MainScreen

@Composable
fun MenuScreen(viewModel: MenuViewModel = getViewModel()) {

    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen(statusBarPadding = false) {
        when (state) {
            is MenuState.Authorization -> {
                AuthorizationScreen(
                    state = state as MenuState.Authorization,
                    onLoginClick = viewModel::authorize,
                    onLoginChange = viewModel::loginInput,
                    onPasswordChange = viewModel::passwordInput,
                    setPasswordVisible = viewModel::setPasswordVisible,
                    setPasswordInvisible = viewModel::setPasswordInvisible
                )
            }
            MenuState.Loading -> LoadingScreen()
            is MenuState.Menu -> {
                MainScreen(
                    state = state as MenuState.Menu,
                    onSignOut = viewModel::logout,
                    onPersonalClick = viewModel::openPersonal,
                    cardClick = viewModel::cardClick,
                )
            }
        }
    }
}
