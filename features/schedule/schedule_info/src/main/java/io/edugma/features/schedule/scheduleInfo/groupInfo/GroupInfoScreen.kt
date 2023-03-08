package io.edugma.features.schedule.scheduleInfo.groupInfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.pxToDp
import io.edugma.features.schedule.domain.model.group.description
import io.edugma.features.schedule.elements.verticalSchedule.VerticalScheduleComponent
import org.koin.androidx.compose.getViewModel

@Composable
fun GroupInfoScreen(viewModel: GroupInfoViewModel = getViewModel(), id: String) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.setId(id)
    }

    GroupInfoContent(
        state = state,
        onBackClick = viewModel::exit,
        onTabSelected = viewModel::onTabSelected,
    )
}

@Composable
private fun GroupInfoContent(
    state: GroupInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<GroupInfoTabs>,
) {
    InfoScaffold(
        title = "Группа " + (state.groupInfo?.title ?: ""),
        onBackClick = onBackClick,
        fields = {
            state.groupInfo?.let { groupInfo ->
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
                            GroupInfoTabs.Schedule -> "Расписание"
                            GroupInfoTabs.Students -> "Студенты"
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
                GroupInfoTabs.Schedule -> {
                    state.scheduleSource?.let {
                        VerticalScheduleComponent(
                            scheduleSource = state.scheduleSource,
                        )
                    }
                }
                GroupInfoTabs.Students -> { }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScaffold(
    title: String,
    onBackClick: ClickListener,
    fields: @Composable ColumnScope.() -> Unit = { },
    tabs: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState(),
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = EdTheme.typography.titleLarge,
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = EdTheme.colorScheme.background,
                    scrolledContainerColor = EdTheme.colorScheme.background,
                ),
                navigationIcon = {
                    // BackIconButton(onBackClick)
                },
            )
        },
        content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(it),
            ) {
                var height by remember {
                    mutableStateOf(0)
                }
                val rate = remember(scrollBehavior.state.heightOffset, scrollBehavior.state.heightOffsetLimit) {
                    scrollBehavior.state.heightOffset / scrollBehavior.state.heightOffsetLimit
                }

                val rateInverse = remember(rate) {
                    1f - rate
                }

                val offset = remember(rate, height) {
                    -height * rate
                }

                val offsetInverse = remember(offset, height) {
                    height + offset
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer { translationY = offsetInverse },
                ) {
                    tabs()
                    content()
                }
                Column(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .offset(y = offset.pxToDp())
                        .alpha(rateInverse)
                        .onGloballyPositioned {
                            height = it.size.height
                        },
                ) {
                    fields()
                }
            }
        },
    )
}
