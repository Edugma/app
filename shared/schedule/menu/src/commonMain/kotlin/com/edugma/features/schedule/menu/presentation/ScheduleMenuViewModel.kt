package com.edugma.features.schedule.menu.presentation

import com.edugma.core.api.utils.nowLocalDate
import com.edugma.core.api.utils.nowLocalTime
import com.edugma.core.api.utils.untilMinutes
import com.edugma.core.arch.mvi.newState
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import com.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import com.edugma.core.navigation.ScheduleScreens
import com.edugma.core.navigation.schedule.ScheduleHistoryScreens
import com.edugma.features.schedule.domain.model.schedule.LessonsByTime
import com.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import com.edugma.features.schedule.domain.usecase.GetClosestLessonsUseCase
import com.edugma.features.schedule.domain.usecase.RemoveSelectedScheduleSourceUseCase
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
import com.edugma.features.schedule.menu.model.MenuItem
import com.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ScheduleMenuViewModel(
    private val useCase: ScheduleUseCase,
    private val getClosestLessonsUseCase: GetClosestLessonsUseCase,
    private val getScheduleMenuItems: GetScheduleMenuItems,
    private val removeSelectedScheduleSourceUseCase: RemoveSelectedScheduleSourceUseCase,
) : BaseActionViewModel<ScheduleMenuUiState, ScheduleMenuAction>(
    ScheduleMenuUiState(),
) {
    init {
//        launchCoroutine {
//            useCase.getCurrentScheduleFlow().onResult(
//                {
//                    val lessons = it.value.let {
//                        useCase.getScheduleDay(it, Clock.System.nowLocalDate())
//                    } ?: emptyList()
//
//                    getCurrentLesson(lessons)
//                },
//                onFailure = {},
//            )
//        }

        launchCoroutine {
            useCase.getSelectedSource().collect { selectedSource ->
                newState {
                    onSelectedSource(selectedSource)
                }
                setMenuItems()
            }
        }
    }

    private fun getCurrentLesson(
        lessons: List<LessonsByTime>,
        now: LocalTime = Clock.System.nowLocalTime(),
    ) {
        val closestLessons = getClosestLessonsUseCase(lessons)
            .map {
                ClosestLessons(
                    now.untilMinutes(it.time.start)
                        .toDuration(DurationUnit.MINUTES),
                    it,
                )
            }.filter { now <= it.lessons.time.end }

        val (notStarted, current) = closestLessons
            .groupBy { it.timeToStart.isNegative() }
            .run {
                getOrElse(false) { emptyList() } to
                    getOrElse(true) { emptyList() }
            }
    }

    private suspend fun setMenuItems() {
        val menuItems = getScheduleMenuItems.invoke()
        newState { copy(menuItems = menuItems) }
    }

    override fun processAction(action: ScheduleMenuAction) {
        when (action) {
            ScheduleMenuAction.OnAppWidgetClick -> {
            }
            ScheduleMenuAction.OnFreePlaceClick -> {
                scheduleRouter.navigateTo(ScheduleScreens.FreePlace())
            }
            ScheduleMenuAction.OnHistoryClick -> {
                scheduleRouter.navigateTo(ScheduleHistoryScreens.Main())
            }
            ScheduleMenuAction.OnLessonsReviewClick -> {
                scheduleRouter.navigateTo(ScheduleScreens.LessonsReview())
            }
            ScheduleMenuAction.OnScheduleCalendarClick -> {
                scheduleRouter.navigateTo(ScheduleScreens.Calendar())
            }
            ScheduleMenuAction.OnScheduleClick -> {
                scheduleRouter.navigateTo(ScheduleScreens.Main())
            }
            ScheduleMenuAction.OnScheduleSourceClick -> {
                scheduleRouter.navigateTo(ScheduleScreens.Source())
            }
            ScheduleMenuAction.OnSignOut -> launchCoroutine {
                removeSelectedScheduleSourceUseCase()
                setMenuItems()
            }
        }
    }

    fun exit() {
        scheduleRouter.back()
    }
}

data class ScheduleMenuUiState(
    val menuItems: List<List<MenuItem>> = emptyList(),
    val main: MainUiState = MainUiState(),
    val source: SourceUiState = SourceUiState(),
    val date: LocalDate = Clock.System.nowLocalDate(),
) {
    fun onSelectedSource(selectedSource: ScheduleSourceFull?) = copy(
        source = source.copy(
            selectedSource = selectedSource,
            accountSelectorVO = selectedSource?.let {
                AccountSelectorVO(
                    title = selectedSource.title,
                    subtitle = selectedSource.description.orEmpty(),
                    avatar = selectedSource.avatar,
                )
            },
        ),
    )

    data class MainUiState(
        val currentLessons: List<ClosestLessons> = emptyList(),
        val notStartedLessons: List<ClosestLessons> = emptyList(),
    )

    data class SourceUiState(
        val selectedSource: ScheduleSourceFull? = null,
        val accountSelectorVO: AccountSelectorVO? = null,
    )
}

class ClosestLessons(
    val timeToStart: Duration,
    val lessons: LessonsByTime,
)
