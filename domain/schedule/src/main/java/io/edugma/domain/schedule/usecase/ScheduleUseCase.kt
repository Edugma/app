package io.edugma.domain.schedule.usecase

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.repository.ScheduleRepository
import io.edugma.domain.schedule.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import java.time.LocalDate

class ScheduleUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository
) {
    fun getSources(type: ScheduleSources) =
        scheduleSourcesRepository.getSources(type)

    fun getSelectedSource() =
        scheduleSourcesRepository.getSelectedSource()

    fun getSchedule(forceUpdate: Boolean = false) =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(Lce.failure<List<ScheduleDay>>(Exception()))
                } else {
                    emitAll(
                        repository.getSchedule(
                            ScheduleSource(source.type, source.key),
                            forceUpdate = forceUpdate
                        )
                    )
                }
            }

    fun getSchedule(scheduleSource: ScheduleSource, forceUpdate: Boolean = false) =
        repository.getSchedule(
            ScheduleSource(scheduleSource.type, scheduleSource.key),
            forceUpdate = forceUpdate
        )

    fun getScheduleDay(schedule: List<ScheduleDay>, date: LocalDate): List<LessonsByTime> {
        return schedule.firstOrNull { it.date == date }?.lessons ?: emptyList()
    }

    fun getTeacher(id: String) =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(null)
                } else {
                    emitAll(repository.getTeacher(ScheduleSource(source.type, source.key), id))
                }
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