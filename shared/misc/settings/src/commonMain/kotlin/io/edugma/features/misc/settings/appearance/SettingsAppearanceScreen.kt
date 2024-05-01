package io.edugma.features.misc.settings.appearance

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import edugma.shared.core.icons.generated.resources.ic_fluent_dark_theme_24_filled
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.api.model.ThemeMode
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.settings.selector.EdSettingsSelector
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.icons.EdIcons
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.ClickListener
import io.edugma.core.utils.viewmodel.getViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsAppearanceScreen(viewModel: SettingsAppearanceViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen(
        statusBarPadding = false,
    ) {
        SettingsAppearanceContent(
            state = state,
            onBackClick = viewModel::exit,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
private fun SettingsAppearanceContent(
    state: SettingsAppearanceUiState,
    onBackClick: ClickListener,
    onAction: (SettingsAppearanceAction) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            EdTopAppBar(
                title = stringResource(Res.string.misc_set_appearance),
                onNavigationClick = onBackClick,
                windowInsets = WindowInsets.statusBars,
                colors = EdTopAppBarDefaults.transparent(),
            )
        }
        SpacerHeight(10.dp)

        Dropdown(
            mode = state.themeMode,
            onThemeModeSelected = {
                onAction(SettingsAppearanceAction.OnThemeModeSelected(it))
            },
        )
    }
}

private fun ThemeMode.getText(): String {
    return when (this) {
        ThemeMode.Light -> "Светлая"
        ThemeMode.Dark -> "Тёмная"
        ThemeMode.System -> "Как в системе"
    }
}

@Composable
private fun Dropdown(
    mode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
) {
    val options = ThemeMode.entries
    var expanded by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.BottomEnd,
    ) {
        EdSettingsSelector(
            text = "Тема",
            onClick = {
                expanded = !expanded
            },
            icon = painterResource(EdIcons.ic_fluent_dark_theme_24_filled),
            selectedText = mode.getText(),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier,
            properties = PopupProperties(),
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { EdLabel(selectionOption.getText()) },
                    onClick = {
                        onThemeModeSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = PaddingValues(8.dp),
                )
            }
        }
    }
}
