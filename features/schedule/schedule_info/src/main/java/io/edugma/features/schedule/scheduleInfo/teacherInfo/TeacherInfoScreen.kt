package io.edugma.features.schedule.scheduleInfo.teacherInfo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.schedule.domain.model.teacher.description
import io.edugma.features.schedule.elements.verticalSchedule.VerticalScheduleComponent
import io.edugma.features.schedule.scheduleInfo.groupInfo.InfoScaffold

@Composable
fun TeacherInfoScreen(viewModel: TeacherInfoViewModel = getViewModel(), id: String) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.setId(id)
    }

    TeacherInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTabSelected = viewModel::onTabSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherInfoContent(
    state: TeacherInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<TeacherInfoTabs>,
) {
    InfoScaffold(
        title = state.teacherInfo?.name ?: "",
        onBackClick = onBackClick,
        fields = {
            state.teacherInfo?.let { groupInfo ->
                EdLabel(
                    text = groupInfo.description,
                    iconPainter = painterResource(EdIcons.ic_fluent_text_description_20_regular),
                )
//                TextIcon(
//                    text = "${groupInfo.course}-й курс",
//                    painter = painterResource(EdIcons.ic_fluent_timer_20_regular)
//                )
//                TextIcon(
//                    text = groupInfo.toString(),
//                    painter = painterResource(EdIcons.ic_fluent_weather_moon_20_regular)
//                )
            }
        },
        tabs = {
            LazyRow(Modifier.fillMaxWidth()) {
                items(state.tabs) {
                    EdCard(
                        onClick = { onTabSelected(it) },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
                        shape = EdTheme.shapes.small,
                    ) {
                        val text = when (it) {
                            TeacherInfoTabs.Schedule -> "Расписание"
                        }

                        Text(
                            text = text,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        },
        content = {
            when (state.selectedTab) {
                TeacherInfoTabs.Schedule -> {
                    state.scheduleSource?.let {
                        VerticalScheduleComponent(
                            scheduleSource = state.scheduleSource,
                        )
                    }
                }
                else -> { }
            }
        },
    )
}
