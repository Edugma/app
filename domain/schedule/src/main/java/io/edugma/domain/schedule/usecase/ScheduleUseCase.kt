package io.edugma.domain.schedule.usecase

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import java.time.LocalDate

class ScheduleUseCase(
    private val repository: ScheduleRepository
) {
    fun getSourceTypes() =
        repository.getSourceTypes()

    fun getSources(type: ScheduleSources) =
        repository.getSources(type)

    suspend fun setSelectedSource(source: ScheduleSourceFull) =
        repository.setSelectedSource(source)

    fun getSelectedSource() =
        repository.getSelectedSource()

    fun getSchedule() =
        repository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(Lce.failure<List<ScheduleDay>>(Exception()))
                } else {
                    emitAll(repository.getSchedule(ScheduleSource(source.type, source.key)))
                }
            }


    fun getLessonsReview() =
        repository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(Result.failure<List<LessonTimesReview>>(Exception()))
                } else {
                    emitAll(repository.getLessonsReview(ScheduleSource(source.type, source.key)))
                }
            }

    fun getScheduleDay(schedule: List<ScheduleDay>, date: LocalDate): List<LessonsByTime> {
        return schedule.firstOrNull { it.date == date }?.lessons ?: emptyList()
    }

    fun getLessonDisplaySettings(scheduleSourceType: ScheduleSources): LessonDisplaySettings {
        return when (scheduleSourceType) {
            ScheduleSources.Group -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = false,
                showPlaces = true
            )
            ScheduleSources.Teacher -> LessonDisplaySettings(
                showTeachers = false,
                showGroups = true,
                showPlaces = true
            )
            ScheduleSources.Student -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = false,
                showPlaces = true
            )
            ScheduleSources.Place -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = false
            )
            ScheduleSources.Subject -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = true
            )
            ScheduleSources.Complex -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = true
            )
        }
    }
}