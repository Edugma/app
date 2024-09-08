package com.edugma.features.schedule.history.presentation.main

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.api.utils.DateFormat
import com.edugma.core.api.utils.TimeFormat
import com.edugma.core.api.utils.formatDate
import com.edugma.core.api.utils.formatTime
import com.edugma.core.arch.mvi.viewmodel.rememberOnAction
import com.edugma.core.designSystem.atoms.card.EdCard
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.button.EdButtonSize
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBarDefaults
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.bottom
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.icons.EdIcons
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.viewmodel.getViewModel
import edugma.shared.core.icons.generated.resources.*
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.resources.generated.resources.Res
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
    state: ScheduleHistoryUiState,
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
                    title = stringResource(Res.string.schedule_history),
                    onNavigationClick = { onAction(ScheduleHistoryAction.OnBack) },
                    windowInsets = WindowInsets.statusBars,
                    colors = EdTopAppBarDefaults.transparent(),
                )
                SpacerHeight(2.dp)
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
        LazyColumn(
            Modifier.fillMaxSize(),
        ) {
            items(state.history) {
                val timestamp = it.timestamp
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 10.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val interactionSource = remember { MutableInteractionSource() }

                    val isSelectedPrevious = state.previousSelected == timestamp
                    val isSelectedNext = state.nextSelected == timestamp
                    val isSelected = isSelectedPrevious || isSelectedNext

                    Column(
                        Modifier.width(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null,
                            interactionSource = interactionSource,
                        )
                        if (isSelected) {
                            val text = if (isSelectedNext) {
                                "след."
                            } else {
                                "пред."
                            }
                            EdLabel(
                                text = text,
                                style = EdTheme.typography.labelMedium,
                            )
                        }
                    }
                    EdCard(
                        onClick = { onAction(ScheduleHistoryAction.OnScheduleSelected(timestamp)) },
                        selected = isSelected,
                        modifier = Modifier.fillMaxWidth(),
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
                            EdLabel(
                                text = "Расписание",
                                style = EdTheme.typography.titleMedium,
                            )
                            SpacerHeight(4.dp)
                            SecondaryContent {
                                EdLabel(
                                    text = date.formatDate(DateFormat.FULL),
                                    iconPainter = painterResource(
                                        EdIcons.ic_fluent_calendar_ltr_16_regular,
                                    ),
                                    style = EdTheme.typography.bodySmall,
                                )
                                EdLabel(
                                    text = date.formatTime(TimeFormat.HOURS_MINUTES),
                                    iconPainter = painterResource(
                                        EdIcons.ic_fluent_clock_16_regular,
                                    ),
                                    style = EdTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
