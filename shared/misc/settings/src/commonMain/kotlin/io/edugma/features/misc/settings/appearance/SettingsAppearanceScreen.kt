package io.edugma.features.misc.settings.appearance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.Typed1Listener

@Composable
fun SettingsAppearanceScreen(viewModel: SettingsAppearanceViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen {
        SettingsAppearanceContent(
            state = state,
            onBackClick = viewModel::exit,
            onNightModeCheckedChange = viewModel::onNightModeCheckedChange,
        )
    }
}

@Composable
private fun SettingsAppearanceContent(
    state: SettingsAppearanceState,
    onBackClick: ClickListener,
    onNightModeCheckedChange: Typed1Listener<NightMode>,
) {
    Column(Modifier.fillMaxSize()) {
        EdTopAppBar(
            title = stringResource(MR.strings.misc_set_appearance),
            onNavigationClick = onBackClick,
        )
        SpacerHeight(50.dp)
        CheckboxItem(
            checked = state.nightMode == NightMode.Light,
            onCheckedChange = { if (it) onNightModeCheckedChange(NightMode.Light) },
        )
    }
}

@Composable
private fun CheckboxItem(
    checked: Boolean,
    onCheckedChange: Typed1Listener<Boolean>,
) {
    Row {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
