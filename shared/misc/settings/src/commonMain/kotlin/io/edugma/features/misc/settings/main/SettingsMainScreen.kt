package io.edugma.features.misc.settings.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.features.misc.settings.resources.MR

@Composable
fun SettingsMainScreen(viewModel: SettingsMainViewModel = getViewModel()) {

    FeatureScreen {
        SettingsMainContent(
            onBackClick = viewModel::exit,
            onAppearanceClick = viewModel::onAppearanceClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainContent(
    onBackClick: io.edugma.core.utils.ClickListener,
    onAppearanceClick: io.edugma.core.utils.ClickListener,
) {
    Column(Modifier.fillMaxSize()) {
        EdTopAppBar(
            title = "Настройки",
            onNavigationClick = onBackClick,
        )
        SpacerHeight(50.dp)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            onClick = onAppearanceClick,
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(MR.strings.misc_set_appearance),
                    modifier = Modifier.align(Alignment.CenterStart),
                )
            }
        }
    }
}
