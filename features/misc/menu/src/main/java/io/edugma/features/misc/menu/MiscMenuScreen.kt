package io.edugma.features.misc.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun MiscMenuScreen(viewModel: MiscMenuViewModel = getViewModel()) {

    FeatureScreen {
        MiscMenuContent(
            onSettingsClick = viewModel::onSettingsClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MiscMenuContent(
    onSettingsClick: ClickListener,
) {
    Column(
        Modifier
            .padding(top = 20.dp, start = 4.dp, end = 4.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = "Прочее",
            style = EdTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp),
        )
        SpacerHeight(height = 20.dp)
        Card(
            onClick = onSettingsClick,
            modifier = Modifier
                .height(150.dp)
                .width(300.dp),
        ) {
            Text(text = "Настройки")
        }
    }
}
