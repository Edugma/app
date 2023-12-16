package io.edugma.features.schedule.domain.usecase

import io.edugma.core.api.utils.LceFlow
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.datetime.LocalDate

class ScheduleUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
) {

    fun getSelectedSource() =
        scheduleSourcesRepository.getSelectedSource()

    suspend fun getCurrentScheduleFlow(forceUpdate: Boolean = false): LceFlow<ScheduleCalendar> {
        val source = scheduleSourcesRepository.getSelectedSourceSuspend() ?: return LceFlow.empty()

        return repository.getSchedule(
            ScheduleSource(source.type, source.id),
            forceUpdate = forceUpdate,
        )
    }

    fun getSchedule(scheduleSource: ScheduleSource, forceUpdate: Boolean = false) =
        repository.getSchedule(
            ScheduleSource(scheduleSource.type, scheduleSource.key),
            forceUpdate = forceUpdate,
        )

    fun getScheduleDay(schedule: List<ScheduleDay>, date: LocalDate): List<LessonEvent> {
        return schedule.firstOrNull { it.date == date }?.lessons ?: emptyList()
    }

    suspend fun getTeacher(id: String): LceFlow<TeacherInfo?> {
        val source = scheduleSourcesRepository.getSelectedSourceSuspend() ?: return LceFlow.empty()

        return repository.getTeacher(ScheduleSource(source.type, source.id), id)
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
