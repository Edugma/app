package io.edugma.features.schedule.utils

import io.edugma.domain.base.utils.insertSeparators
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.features.schedule.model.ScheduleDayUiModel
import io.edugma.features.schedule.model.ScheduleItem
import java.time.temporal.ChronoUnit

@JvmName("toUiModelScheduleDay")
fun List<ScheduleDay>.toUiModel(): List<ScheduleDayUiModel> {
    return map {
        ScheduleDayUiModel(
            date = it.date,
            lessons = it.lessons.toUiModel()
        )
    }
}

@JvmName("toUiModelLessonsByTime")
fun List<LessonsByTime>.toUiModel(): List<ScheduleItem> {
    return map { ScheduleItem.LessonByTime(it) }
        .insertSeparators { before, next ->
            when {
                before == null || next == null -> null
                else -> getWindowOrNull(before.lesson.time, next.lesson.time)
            }
        }
}

private fun getWindowOrNull(before: LessonTime, next: LessonTime): ScheduleItem.Window? {
    val totalMinutes = before.end.until(next.start, ChronoUnit.MINUTES)
    return if (totalMinutes < 15) {
        null
    } else {
        ScheduleItem.Window(
            timeFrom = before.end,
            timeTo = next.start,
            totalMinutes = totalMinutes
        )
    }
}