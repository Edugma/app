package io.edugma.features.misc.settings.appearance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener
import io.edugma.features.misc.settings.R

@Composable
fun SettingsAppearanceScreen(viewModel: SettingsAppearanceViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    FeatureScreen {
        SettingsAppearanceContent(
            state = state,
            onBackClick = viewModel::exit,
            onNightModeCheckedChange = viewModel::onNightModeCheckedChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsAppearanceContent(
    state: SettingsAppearanceState,
    onBackClick: io.edugma.core.utils.ClickListener,
    onNightModeCheckedChange: io.edugma.core.utils.Typed1Listener<NightMode>,
) {
    Column(Modifier.fillMaxSize()) {
        EdTopAppBar(
            title = stringResource(R.string.misc_set_appearance),
            onNavigationClick = onBackClick,
        )
        SpacerHeight(50.dp)
        CheckboxItem(
            checked = state.nightMode == NightMode.Light,
            onCheckedChange = { if (it) onNightModeCheckedChange(NightMode.Light) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckboxItem(
    checked: Boolean,
    onCheckedChange: io.edugma.core.utils.Typed1Listener<Boolean>,
) {
    Row {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
