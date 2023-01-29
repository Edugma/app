package io.edugma.features.misc.settings.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.misc.settings.R
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsMainScreen(viewModel: SettingsMainViewModel = getViewModel()) {

    SettingsMainContent(
        onBackClick = viewModel::exit,
        onAppearanceClick = viewModel::onAppearanceClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainContent(
    onBackClick: ClickListener,
    onAppearanceClick: ClickListener,
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
                    text = stringResource(R.string.misc_set_appearance),
                    modifier = Modifier.align(Alignment.CenterStart),
                )
            }
        }
    }
}
