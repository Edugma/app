package com.edugma.features.misc.aboutApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.organism.EdScaffold
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.top
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.designSystem.utils.rememberCachedIconPainter
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel

@Composable
fun AboutAppScreen(viewModel: AboutAppViewModel = getViewModel()) {
    val state by viewModel.collectAsState()

    val onAction = viewModel.rememberOnAction()

    FeatureScreen(
        statusBarPadding = false,
        navigationBarPadding = true,
    ) {
        AddAccountContent(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun AddAccountContent(
    state: AboutAppUiState,
    onAction: (AboutAppAction) -> Unit,
) {
    EdScaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            EdTopAppBar(
                title = "Об Edugma",
                onNavigationClick = {
                    onAction(AboutAppAction.OnBack)
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
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .size(150.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    painter = rememberCachedIconPainter("https://edugma.com/assets/img/edugma-logo.png"),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                )
                EdLabel(
                    "Версия приложения",
                    style = EdTheme.typography.titleMedium,
                )
                SpacerHeight(4.dp)
                EdLabel(
                    state.version.orEmpty() + state.buildType?.let { "-$it" }.orEmpty(),
                    style = EdTheme.typography.titleLarge,
                )
                SpacerHeight(16.dp)
                EdLabel(
                    "Социальные сети",
                    style = EdTheme.typography.titleMedium,
                )
                SpacerHeight(4.dp)
                SecondaryContent {
                    EdLabel(
                        "Здесь вы сможете узнать об обновлениях, написать нам о найденных багах или предложениях по улучшению",
                        style = EdTheme.typography.bodyMedium,
                    )
                }
                SpacerHeight(8.dp)
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    IconButton(
                        modifier = Modifier.size(75.dp),
                        onClick = {
                            onAction(AboutAppAction.TelegramClick)
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = rememberCachedIconPainter("https://img.icons8.com/fluency/96/telegram-app.png"),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                        )
                    }
                    IconButton(
                        modifier = Modifier.size(75.dp),
                        onClick = {
                            onAction(AboutAppAction.VkClick)
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(50.dp),
                            painter = rememberCachedIconPainter("https://img.icons8.com/fluency/96/vk-circled.png"),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                        )
                    }
                }
                SpacerHeight(32.dp)
            }
        }
    }
}
