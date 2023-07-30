package io.edugma.features.schedule.menu

import io.edugma.core.api.utils.nowLocalDate
import io.edugma.core.api.utils.nowLocalTime
import io.edugma.core.api.utils.untilMinutes
import io.edugma.core.arch.mvi.updateState
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.core.navigation.schedule.ScheduleHistoryScreens
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.usecase.GetClosestLessonsUseCase
import io.edugma.features.schedule.domain.usecase.RemoveSelectedScheduleSourceUseCase
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import io.edugma.features.schedule.menu.model.MenuItem
import io.edugma.features.schedule.menu.usecase.GetScheduleMenuItems
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ScheduleMenuViewModel(
    private val useCase: ScheduleUseCase,
    private val getClosestLessonsUseCase: GetClosestLessonsUseCase,
    private val getScheduleMenuItems: GetScheduleMenuItems,
    private val removeSelectedScheduleSourceUseCase: RemoveSelectedScheduleSourceUseCase,
) : BaseViewModel<ScheduleMenuState>(
    ScheduleMenuState(),
) {
    init {
        launchCoroutine {
            useCase.getSchedule().collect {
                val lessons = it.getOrNull()?.let {
                    useCase.getScheduleDay(it, Clock.System.nowLocalDate())
                } ?: emptyList()

                val now = Clock.System.nowLocalTime()
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
        }

        launchCoroutine {
            useCase.getSelectedSource().collect { selectedSource ->
                updateState {
                    copy(
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
                }
                setMenuItems()
            }
        }

        launchCoroutine {
            setMenuItems()
        }
    }

    private suspend fun setMenuItems() {
        val menuItems = getScheduleMenuItems.invoke()
        updateState { copy(menuItems = menuItems) }
    }

    fun onScheduleClick() {
        router.navigateTo(ScheduleScreens.Main())
    }

    fun onLessonsReviewClick() {
        router.navigateTo(ScheduleScreens.LessonsReview())
    }

    fun onScheduleCalendarClick() {
        router.navigateTo(ScheduleScreens.Calendar())
    }

    fun onScheduleSourceClick() {
        router.navigateTo(ScheduleScreens.Source())
    }

    fun onFreePlaceClick() {
        router.navigateTo(ScheduleScreens.FreePlace())
    }

    fun onAppWidgetClick() {
    }

    fun onHistoryClick() {
        router.navigateTo(ScheduleHistoryScreens.Main())
    }

    fun onSignOut() {
        launchCoroutine {
            removeSelectedScheduleSourceUseCase()
            setMenuItems()
        }
    }
}

data class ScheduleMenuState(
    val menuItems: List<List<MenuItem>> = emptyList(),
    val main: MainState = MainState(),
    val source: SourceState = SourceState(),
    val date: LocalDate = Clock.System.nowLocalDate(),
) {
    data class MainState(
        val currentLessons: List<ClosestLessons> = emptyList(),
        val notStartedLessons: List<ClosestLessons> = emptyList(),
    )

    data class SourceState(
        val selectedSource: ScheduleSourceFull? = null,
        val accountSelectorVO: AccountSelectorVO? = null,
    )
}

class ClosestLessons(
    val timeToStart: Duration,
    val lessons: LessonsByTime,
)
