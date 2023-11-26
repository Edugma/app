package io.edugma.features.schedule.menu.presentation

import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.api.utils.nowLocalTime
import io.edugma.core.api.utils.untilMinutes
import io.edugma.core.arch.mvi.newState
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.usecase.GetClosestLessonsUseCase
import io.edugma.features.schedule.domain.usecase.RemoveSelectedScheduleSourceUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import io.edugma.features.schedule.menu.model.MenuItem
import io.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
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
        launchCoroutine {
            useCase.getSchedule().collect {
                val lessons = it.getOrNull()?.let {
                    useCase.getScheduleDay(it, Clock.System.nowLocalDate())
                } ?: emptyList()

                getCurrentLesson(lessons)
            }
        }

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

    override fun onAction(action: ScheduleMenuAction) {
        when (action) {
            ScheduleMenuAction.OnAppWidgetClick -> {
            }
            ScheduleMenuAction.OnFreePlaceClick -> {
                router.navigateTo(ScheduleScreens.FreePlace())
            }
            ScheduleMenuAction.OnHistoryClick -> {
                router.navigateTo(ScheduleHistoryScreens.Main())
            }
            ScheduleMenuAction.OnLessonsReviewClick -> {
                router.navigateTo(ScheduleScreens.LessonsReview())
            }
            ScheduleMenuAction.OnScheduleCalendarClick -> {
                router.navigateTo(ScheduleScreens.Calendar())
            }
            ScheduleMenuAction.OnScheduleClick -> {
                router.navigateTo(ScheduleScreens.Main())
            }
            ScheduleMenuAction.OnScheduleSourceClick -> {
                router.navigateTo(ScheduleScreens.Source())
            }
            ScheduleMenuAction.OnSignOut -> launchCoroutine {
                removeSelectedScheduleSourceUseCase()
                setMenuItems()
            }
        }
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
                    subtitle = selectedSource.description,
                    avatar = selectedSource.avatarUrl,
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
