package io.edugma.features.schedule.domain.model.schedule

import io.edugma.domain.base.utils.converters.LocalDateConverter
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class ScheduleDay(
    @Serializable(with = LocalDateConverter::class)
    val date: LocalDate,
    val lessons: List<LessonsByTime>,
)
