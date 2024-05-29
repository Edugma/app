package com.edugma.features.schedule.elements.utils

import com.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import com.edugma.features.schedule.elements.model.ScheduleCalendarUiModel
import kotlin.jvm.JvmName

@JvmName("toUiModelScheduleDay")
fun ScheduleCalendar.toUiModel(): ScheduleCalendarUiModel {
    return ScheduleCalendarUiModel(
        scheduleCalendar = this,
    )
}

// TODO

// @JvmName("toUiModelLessonsByTime")
// fun List<LessonEvent>.toUiModel(): List<ScheduleItem> {
//    return map { ScheduleItem.LessonEventUiModel(it) }
//        .insertSeparators { before, next ->
//            when {
//                before == null || next == null -> null
//                else -> getWindowOrNull(before.lesson2.time, next.lesson2.time)
//            }
//        }
// }
//
// private fun getWindowOrNull(before: LessonTime, next: LessonTime): ScheduleItem.Window? {
//    val totalMinutes = before.end.untilMinutes(next.start)
//    return if (totalMinutes < 15) {
//        null
//    } else {
//        ScheduleItem.Window(
//            timeFrom = before.end,
//            timeTo = next.start,
//            totalMinutes = totalMinutes,
//        )
//    }
// }
