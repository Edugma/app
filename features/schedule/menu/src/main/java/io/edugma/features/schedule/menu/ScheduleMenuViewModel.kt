package io.edugma.features.schedule.menu

import androidx.lifecycle.viewModelScope
import io.edugma.core.designSystem.organism.accountSelector.AccountSelectorVO
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.domain.schedule.utils.getClosestLessons
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.BaseViewModelFull
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.base.navigation.schedule.ScheduleHistoryScreens
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ScheduleMenuViewModel(
    private val useCase: ScheduleUseCase,
) : BaseViewModelFull<ScheduleMenuState, ScheduleMenuMutator, Nothing>(
    ScheduleMenuState(),
    ::ScheduleMenuMutator,
) {
    init {
        viewModelScope.launch {
            useCase.getSchedule().collect {
                val lessons = it.getOrNull()?.let {
                    useCase.getScheduleDay(it, LocalDate.now())
                } ?: emptyList()

                val now = LocalTime.now()
                val closestLessons = getClosestLessons(lessons)
                    .map {
                        ClosestLessons(
                            now.until(it.time.start, ChronoUnit.MINUTES)
                                .toDuration(DurationUnit.MINUTES),
                            it,
                        )
                    }.filter { now <= it.lessons.time.end }

                val (notStarted, current) = closestLessons
                    .groupBy { it.timeToStart.isNegative() }
                    .run {
                        getOrDefault(false, emptyList()) to
                            getOrDefault(true, emptyList())
                    }
            }
        }

        viewModelScope.launch {
            useCase.getSelectedSource().collect {
                mutateState { setSelectedSource(it.getOrNull()) }
            }
        }
    }

    fun onScheduleClick() {
        router.navigateTo(ScheduleScreens.Main())
    }

    fun onLessonsReviewClick() {
        router.navigateTo(ScheduleScreens.LessonsReview)
    }

    fun onScheduleCalendarClick() {
        router.navigateTo(ScheduleScreens.Calendar)
    }

    fun onScheduleSourceClick() {
        router.navigateTo(ScheduleScreens.Source)
    }

    fun onFreePlaceClick() {
        router.navigateTo(ScheduleScreens.FreePlace)
    }

    fun onAppWidgetClick() {
    }

    fun onHistoryClick() {
        router.navigateTo(ScheduleHistoryScreens.Main)
    }
}

data class ScheduleMenuState(
    val main: MainState = MainState(),
    val source: SourceState = SourceState(),
    val date: LocalDate = LocalDate.now(),
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

class ScheduleMenuMutator : BaseMutator<ScheduleMenuState>() {
    fun setSelectedSource(selectedSource: ScheduleSourceFull?) =
        set(state.source.selectedSource, selectedSource) {
            copy(
                source = source.copy(
                    selectedSource = it,
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
}

class ClosestLessons(
    val timeToStart: Duration,
    val lessons: LessonsByTime,
)
