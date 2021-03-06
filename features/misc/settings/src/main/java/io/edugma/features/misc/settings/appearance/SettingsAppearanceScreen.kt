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
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.elements.PrimaryTopAppBar
import io.edugma.features.base.elements.SpacerHeight
import io.edugma.features.misc.settings.R
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsAppearanceScreen(viewModel: SettingsAppearanceViewModel = getViewModel()) {
    val state by viewModel.state.collectAsState()

    SettingsAppearanceContent(
        state = state,
        onBackClick = viewModel::exit,
        onNightModeCheckedChange = viewModel::onNightModeCheckedChange
    )
}

@Composable
private fun SettingsAppearanceContent(
    state: SettingsAppearanceState,
    onBackClick: ClickListener,
    onNightModeCheckedChange: Typed1Listener<NightMode>
) {
    Column(Modifier.fillMaxSize()) {
        PrimaryTopAppBar(
            title = stringResource(R.string.misc_set_appearance),
            onBackClick = onBackClick
        )
        SpacerHeight(50.dp)
        CheckboxItem(
            checked = state.nightMode == NightMode.Light,
            onCheckedChange = { if (it) onNightModeCheckedChange(NightMode.Light) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckboxItem(
    checked: Boolean,
    onCheckedChange: Typed1Listener<Boolean>
) {
    Row {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}