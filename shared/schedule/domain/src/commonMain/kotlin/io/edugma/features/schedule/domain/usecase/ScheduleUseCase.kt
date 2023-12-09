package io.edugma.features.schedule.domain.usecase

import io.edugma.core.api.utils.Lce
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import kotlinx.datetime.LocalDate

class ScheduleUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {

    fun getSelectedSource() =
        scheduleSourcesRepository.getSelectedSource()

    fun getSchedule(forceUpdate: Boolean = false) =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest { source ->
                if (source == null) {
                    emit(Lce.failure<List<ScheduleDay>>(Exception()))
                } else {
                    emitAll(
                        repository.getSchedule(
                            ScheduleSource(source.type, source.id),
                            forceUpdate = forceUpdate,
                        ),
                    )
                }
            }

    fun getSchedule(scheduleSource: ScheduleSource, forceUpdate: Boolean = false) =
        repository.getSchedule(
            ScheduleSource(scheduleSource.type, scheduleSource.key),
            forceUpdate = forceUpdate,
        )

    fun getScheduleDay(schedule: List<ScheduleDay>, date: LocalDate): List<LessonsByTime> {
        return schedule.firstOrNull { it.date == date }?.lessons ?: emptyList()
    }

    fun getTeacher(id: String) =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest { source ->
                if (source == null) {
                    emit(null)
                } else {
                    emitAll(repository.getTeacher(ScheduleSource(source.type, source.id), id))
                }
            }

    fun getLessonDisplaySettings(scheduleSourceType: String): LessonDisplaySettings {
        return when (scheduleSourceType) {
            "group" -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = false,
                showPlaces = true,
            )
            "teacher" -> LessonDisplaySettings(
                showTeachers = false,
                showGroups = true,
                showPlaces = true,
            )
            "student" -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = false,
                showPlaces = true,
            )
            "place" -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = false,
            )
            "subject" -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = true,
            )
            ScheduleSourceType.COMPLEX -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = true,
            )
            else -> LessonDisplaySettings(
                showTeachers = true,
                showGroups = true,
                showPlaces = true,
            )
        }
    }
}
