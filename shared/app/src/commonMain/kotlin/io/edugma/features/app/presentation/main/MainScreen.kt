package io.edugma.features.app.presentation.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.Ref
import androidx.compose.ui.unit.dp
import io.edugma.core.api.model.SnackbarCommand
import io.edugma.core.api.model.ThemeMode
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.snackbar.EdSnackbar
import io.edugma.core.designSystem.organism.snackbar.EdSnackbarStyle
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.utils.LocalEdIconLoader
import io.edugma.core.designSystem.utils.LocalEdImageLoader
import io.edugma.core.designSystem.utils.statusBarsPadding
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.features.app.presentation.main.widgets.BottomNav
import io.edugma.features.app.presentation.main.widgets.rememberTabNavigator
import io.edugma.navigation.core.compose.EdugmaTabNavigation
import kotlin.time.Duration.Companion.seconds

@Composable
fun MainScreen(
    viewModel: MainAppViewModel = getViewModel(),
) {
    val state by viewModel.stateFlow.collectAsState()

    EdTheme(
        useDynamicColors = true,
        useDarkTheme = when (state.themeMode) {
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
            ThemeMode.System -> isSystemInDarkTheme()
        },
    ) {
        EdSurface(
            color = EdTheme.colorScheme.background,
            elevation = EdElevation.Level0,
        ) {
            MainContent()
        }
    }
}

@Composable
fun MainContent(
    viewModel: MainViewModel = getViewModel(),
) {
    val (tabNavigator, isNavigationBarVisible) = rememberTabNavigator(viewModel)

    val state by viewModel.stateFlow.collectAsState()

    val onAction = viewModel.rememberOnAction()

    CompositionLocalProvider(
        LocalEdImageLoader provides viewModel.commonImageLoader,
        LocalEdIconLoader provides viewModel.iconImageLoader,
    ) {
        Scaffold(
            bottomBar = { BottomNav(tabNavigator, isNavigationBarVisible) },
            contentWindowInsets = WindowInsets(0.dp),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                EdugmaTabNavigation(tabNavigator)
                Snackbar(
                    messageProvider = { state.snackbars.firstOrNull() },
                    onDismissed = {
                        onAction(MainAction.OnSnackbarDismissed(it))
                    },
                    onAction = {
                        onAction(MainAction.OnSnackbarActionClicked(it))
                    },
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Snackbar(
    messageProvider: () -> SnackbarCommand.Message?,
    onDismissed: (SnackbarCommand.Message) -> Unit,
    onAction: (SnackbarCommand.Message) -> Unit,
) {
    val message = messageProvider()

    val messageRef = remember {
        Ref<SnackbarCommand.Message>()
    }

    if (message != null) {
        messageRef.value = message
    }

    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut(),
    ) {
        val message = messageRef.value!!
        val title = message.title.ifEmpty {
            when (message.type) {
                SnackbarCommand.Message.Type.Info -> "Важная информация"
                SnackbarCommand.Message.Type.Warning -> "Предупреждение"
                SnackbarCommand.Message.Type.Error -> "Ошибка"
            }
        }

        val style = when (message.type) {
            SnackbarCommand.Message.Type.Info -> EdSnackbarStyle.default
            SnackbarCommand.Message.Type.Warning -> EdSnackbarStyle.warning
            SnackbarCommand.Message.Type.Error -> EdSnackbarStyle.error
        }

        EdSnackbar(
            title = title,
            subtitle = message.subtitle,
            action = message.action.takeIf { message.needResult },
            // TODO
            // timeToDismiss = message.timeToDismiss ?: 6.seconds,
            timeToDismiss = 6.seconds,
            onDismissed = {
                onDismissed(message)
            },
            onActionClick = if (message.needResult) {
                { onAction(message) }
            } else {
                null
            },
            style = style,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 32.dp),
        )
    }
}
