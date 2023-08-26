package io.edugma.features.misc.settings.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener

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
    onBackClick: io.edugma.core.utils.ClickListener,
    onAppearanceClick: io.edugma.core.utils.ClickListener,
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
        SettingsItem(
            text = stringResource(MR.strings.misc_set_appearance),
            onClick = onAppearanceClick,
        )
    }
}

@Composable
private fun SettingsItem(
    text: String,
    onClick: () -> Unit,
) {
    EdSurface(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick,
        shape = EdTheme.shapes.medium,
    ) {
        Row(
            Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EdLabel(
                text = text,
                modifier = Modifier.padding(start = 16.dp).weight(1f),
            )
            Image(
                painter = painterResource(EdIcons.ic_fluent_chevron_right_20_filled),
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
    }
}
