package com.edugma.features.misc.settings.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.settings.button.EdSettingsButton
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.bottom
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.getViewModel

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
            text = stringResource(Res.string.misc_set_appearance),
            icon = painterResource(EdIcons.ic_fluent_paint_brush_24_filled),
            onClick = onAppearanceClick,
        )
    }
}
