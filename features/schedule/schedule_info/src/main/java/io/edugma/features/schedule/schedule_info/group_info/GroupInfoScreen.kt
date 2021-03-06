package io.edugma.features.schedule.schedule_info.group_info

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.*
import io.edugma.features.base.elements.BackIconButton
import io.edugma.features.base.elements.TextIcon
import io.edugma.features.base.elements.TonalCard
import io.edugma.features.schedule.elements.vertical_schedule.VerticalScheduleComponent
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
        onTabSelected = viewModel::onTabSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GroupInfoContent(
    state: GroupInfoState,
    onBackClick: ClickListener,
    onTabSelected: Typed1Listener<GroupInfoTabs>
) {
    InfoScaffold(
        title = "Группа " + (state.groupInfo?.title ?: ""),
        onBackClick = onBackClick,
        fields = {
            state.groupInfo?.let { groupInfo ->
                TextIcon(
                    text = groupInfo.description,
                    painter = painterResource(FluentIcons.ic_fluent_text_description_20_regular)
                )
                TextIcon(
                    text = "${groupInfo.course}-й курс",
                    painter = painterResource(FluentIcons.ic_fluent_timer_20_regular)
                )
                TextIcon(
                    text = if (groupInfo.isEvening) "Вечерняя" else "Дневная",
                    painter = painterResource(FluentIcons.ic_fluent_weather_moon_20_regular)
                )
            }
        },
        tabs = {
            LazyRow(Modifier.fillMaxWidth()) {
                items(state.tabs) {
                    TonalCard(
                        onClick = { onTabSelected(it) },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
                        shape = MaterialTheme3.shapes.small
                    ) {
                        val text = when (it) {
                            GroupInfoTabs.Schedule -> "Расписание"
                            GroupInfoTabs.Students -> "Студенты"
                        }

                        Text(
                            text = text,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
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
                            scheduleSource = state.scheduleSource
                        )
                    }
                }
                GroupInfoTabs.Students -> { }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScaffold(
    title: String,
    onBackClick: ClickListener,
    fields: @Composable ColumnScope.() -> Unit = { },
    tabs: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarScrollState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme3.typography.titleLarge
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme3.colorScheme.background,
                    scrolledContainerColor = MaterialTheme3.colorScheme.background,
                ),
                navigationIcon = {
                    BackIconButton(onBackClick)
                }
            )
        },
        content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                var height by remember {
                    mutableStateOf(0)
                }
                val rate = remember(scrollBehavior.state.offset, scrollBehavior.state.offsetLimit) {
                    scrollBehavior.state.offset / scrollBehavior.state.offsetLimit
                }

                val rateInverse = remember(rate) {
                    1f - rate
                }

                val offset = remember(rate, height) {
                    - height * rate
                }

                val offsetInverse = remember(offset, height) {
                    height + offset
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { translationY = offsetInverse }
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
                        }
                ) {
                    fields()
                }

            }
        }
    )
}