package com.mospolytech.domain.schedule.usecase

import com.mospolytech.domain.schedule.model.place.PlaceFilters
import com.mospolytech.domain.schedule.model.schedule.LessonsByTime
import com.mospolytech.domain.schedule.model.schedule.ScheduleDay
import com.mospolytech.domain.schedule.model.source.ScheduleSource
import com.mospolytech.domain.schedule.model.source.ScheduleSources
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import java.time.LocalDate

class ScheduleUseCase(
    private val repository: ScheduleRepository
) {
    fun getSourceTypes() =
        repository.getSourceTypes()

    fun getSources(type: ScheduleSources) =
        repository.getSources(type)

    fun getSchedule() =
        repository.getSchedule(ScheduleSource(ScheduleSources.Group, "181-721"))

    fun getLessonsReview() =
        repository.getLessonsReview(ScheduleSource(ScheduleSources.Group, "181-721"))

    fun findFreePlaces(filters: PlaceFilters) =
        repository.findFreePlaces(filters)

    fun getScheduleDay(schedule: List<ScheduleDay>, date: LocalDate): List<LessonsByTime> {
        return schedule.firstOrNull { it.date == date }?.lessons ?: emptyList()
    }
}