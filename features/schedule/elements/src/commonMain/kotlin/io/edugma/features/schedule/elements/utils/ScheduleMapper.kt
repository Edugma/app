package io.edugma.features.schedule.elements.utils

import io.edugma.core.api.utils.insertSeparators
import io.edugma.core.api.utils.untilMinutes
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.elements.lesson.model.ScheduleItem
import io.edugma.features.schedule.elements.model.ScheduleDayUiModel
import kotlin.jvm.JvmName

@JvmName("toUiModelScheduleDay")
fun List<ScheduleDay>.toUiModel(): List<ScheduleDayUiModel> {
    return map {
        ScheduleDayUiModel(
            date = it.date,
            lessons = it.lessons.toUiModel(),
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
    val totalMinutes = before.end.untilMinutes(next.start)
    return if (totalMinutes < 15) {
        null
    } else {
        ScheduleItem.Window(
            timeFrom = before.end,
            timeTo = next.start,
            totalMinutes = totalMinutes,
        )
    }
}
