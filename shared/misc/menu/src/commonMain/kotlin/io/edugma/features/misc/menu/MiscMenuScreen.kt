package io.edugma.features.misc.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerFill
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.iconCard.EdIconCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.utils.rememberCachedIconPainter
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.viewmodel.getViewModel

@Composable
fun MiscMenuScreen(viewModel: MiscMenuViewModel = getViewModel()) {
    FeatureScreen(
        statusBarPadding = false,
    ) {
        MiscMenuContent(
            onSettingsClick = viewModel::onSettingsClick,
            onNodeClick = viewModel::onNodeClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MiscMenuContent(
    onSettingsClick: () -> Unit,
    onNodeClick: () -> Unit,
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 20.dp, start = 4.dp, end = 4.dp),
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = "Прочее",
                        style = EdTheme.typography.headlineMedium,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f),
                    )
                }
                SpacerHeight(height = 14.dp)
            }
        }

        SpacerHeight(height = 10.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EdIconCard(
                title = "Настройки\n",
                onClick = onSettingsClick,
                modifier = Modifier.weight(1f),
                icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/settings.png"),
            )
            EdIconCard(
                title = "Сервер\n",
                onClick = onNodeClick,
                modifier = Modifier.weight(1f),
                icon = rememberCachedIconPainter("https://img.icons8.com/fluency/48/server--v1.png"),
            )
        }
        SpacerHeight(height = 8.dp)
    }
}
