package io.edugma.features.misc.settings.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.settings.button.EdSettingsButton
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.viewmodel.getViewModel

@Composable
fun SettingsMainScreen(viewModel: SettingsMainViewModel = getViewModel()) {

    FeatureScreen(
        statusBarPadding = false,
    ) {
        SettingsMainContent(
            onBackClick = viewModel::exit,
            onAppearanceClick = viewModel::onAppearanceClick,
        )
    }
}

@Composable
fun SettingsMainContent(
    onBackClick: () -> Unit,
    onAppearanceClick: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            EdTopAppBar(
                title = "Настройки",
                onNavigationClick = onBackClick,
                windowInsets = WindowInsets.statusBars,
                colors = EdTopAppBarDefaults.transparent(),
            )
        }
        SpacerHeight(10.dp)
        EdSettingsButton(
            text = stringResource(MR.strings.misc_set_appearance),
            icon = painterResource(EdIcons.ic_fluent_paint_brush_24_filled),
            onClick = onAppearanceClick,
        )
    }
}
