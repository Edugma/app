package io.edugma.features.app.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.organism.snackbar.EdSnackbar
import io.edugma.core.designSystem.utils.LocalEdIconLoader
import io.edugma.core.designSystem.utils.LocalEdImageLoader
import io.edugma.core.designSystem.utils.statusBarsPadding
import io.edugma.core.utils.viewmodel.getViewModel
import io.edugma.navigation.core.compose.EdugmaTabNavigation

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
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Snackbar(
    messageProvider: () -> SnackbarCommand.Message?,
    onDismissed: (SnackbarCommand.Message) -> Unit,
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

        EdSnackbar(
            title = title,
            subtitle = message.subtitle,
            onDismissed = {
                onDismissed(message)
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .statusBarsPadding()
                .fillMaxWidth(),
        )
    }
}
