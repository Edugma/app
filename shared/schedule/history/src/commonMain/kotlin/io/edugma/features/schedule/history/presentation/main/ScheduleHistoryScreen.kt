package io.edugma.features.schedule.history.presentation.main

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moriatsushi.insetsx.statusBars
import com.moriatsushi.insetsx.statusBarsPadding
import dev.icerock.moko.resources.compose.stringResource
import io.edugma.core.api.utils.format
import io.edugma.core.arch.mvi.viewmodel.rememberOnAction
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.button.EdButtonSize
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.bottom
import io.edugma.core.designSystem.utils.SecondaryContent
import io.edugma.core.resources.MR
import io.edugma.core.ui.screen.FeatureScreen
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ScheduleHistoryScreen(viewModel: ScheduleHistoryViewModel = getViewModel()) {
    val state by viewModel.stateFlow.collectAsState()

    FeatureScreen(
        statusBarPadding = false,
    ) {
        ScheduleHistoryContent(
            state = state,
            onAction = viewModel.rememberOnAction(),
        )
    }
}

@Composable
private fun ScheduleHistoryContent(
    state: ScheduleHistoryState,
    onAction: (ScheduleHistoryAction) -> Unit,
) {
    Column(
        Modifier.fillMaxSize(),
    ) {
        EdSurface(
            shape = EdTheme.shapes.large.bottom(),
        ) {
            Column(Modifier.fillMaxWidth().statusBarsPadding()) {
                EdTopAppBar(
                    title = stringResource(MR.strings.schedule_history),
                    onNavigationClick = { onAction(ScheduleHistoryAction.OnBack) },
                    windowInsets = WindowInsets.statusBars,
                    colors = EdTopAppBarDefaults.transparent(),
                )
                SpacerHeight(4.dp)
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SecondaryContent {
                        EdLabel(
                            "Выберите две версии расписания, чтобы сравнить их",
                            modifier = Modifier.padding(horizontal = 16.dp).weight(1f),
                            style = EdTheme.typography.bodySmall,
                        )
                    }
                    SpacerWidth(4.dp)
                    EdButton(
                        text = "Сравнить",
                        onClick = {
                            onAction(ScheduleHistoryAction.OnCompareClicked)
                        },
                        size = EdButtonSize.small,
                        enabled = state.isCheckButtonEnabled,
                        modifier = Modifier
                            .padding(horizontal = 16.dp),
                    )
                }
                SpacerHeight(8.dp)
            }
        }
        state.history.forEach { (schedule, timestamp) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 16.dp)) {
                val interactionSource = remember { MutableInteractionSource() }

                val isSelected = state.firstSelected == timestamp ||
                    state.secondSelected == timestamp

                RadioButton(
                    selected = isSelected,
                    onClick = null,
                    interactionSource = interactionSource,
                )
                SpacerWidth(8.dp)
                EdCard(
                    onClick = { onAction(ScheduleHistoryAction.OnScheduleSelected(timestamp)) },
                    selected = isSelected,
                    modifier = Modifier
                        .fillMaxWidth(),
                    interactionSource = interactionSource,
                ) {
                    Column(
                        Modifier
                            .padding(vertical = 10.dp, horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        val date = remember(timestamp) {
                            timestamp.toLocalDateTime(TimeZone.currentSystemDefault())
                        }
                        Text(text = date.format("dd MMMM yyyy, HH:mm"))
                    }
                }
            }
        }
    }
}
