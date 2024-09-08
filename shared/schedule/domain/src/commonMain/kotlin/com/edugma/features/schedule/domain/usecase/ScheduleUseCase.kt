package com.edugma.features.schedule.domain.usecase

import com.edugma.core.api.utils.LceFlow
import com.edugma.core.api.utils.first
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import com.edugma.features.schedule.domain.model.schedule.ScheduleDay
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.model.source.ScheduleSourceType
import com.edugma.features.schedule.domain.model.teacher.TeacherInfo
import com.edugma.features.schedule.domain.repository.ScheduleRepository
import com.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
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

    suspend fun getEvent(eventId: String): LessonEvent? {
        return getCurrentScheduleFlow()
            .first()
            .getOrNull()
            ?.findEvent(eventId)
    }

    suspend fun getLocalPlaceInfo(id: String): CompactPlaceInfo? {
        val source = scheduleSourcesRepository.getSelectedSourceSuspend() ?: return null

        val rawSchedule = repository.getRawSchedule(ScheduleSource(source.type, source.id))

        return rawSchedule.first().getOrNull()?.places?.firstOrNull { it.id == id }
    }
}
