package com.edugma.features.schedule.elements.model

import co.touchlab.kermit.Logger
import com.edugma.core.api.utils.untilMinutes
import com.edugma.features.schedule.domain.model.compact.zonedTime
import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import com.edugma.features.schedule.domain.model.schedule.ScheduleDay
import com.edugma.features.schedule.elements.lesson.model.ScheduleEventUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone

class ScheduleCalendarUiModel(
    val scheduleCalendar: ScheduleCalendar,
) {

    val size: Int
        get() = scheduleCalendar.size

    fun init(
        today: LocalDate,
        size: Int,
        todayIndex: Int,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ) {
        scheduleCalendar.init(
            today,
            size,
            todayIndex,
            timeZone,
        )
    }

    operator fun get(index: Int): ScheduleDayUiModel {
        return scheduleCalendar.get(index).toUiModel()
    }

    operator fun get(date: LocalDate): ScheduleDayUiModel {
        return scheduleCalendar.get(date).toUiModel()
    }

    private fun ScheduleDay.toUiModel(): ScheduleDayUiModel {
        val assumedSize = if (this.lessons.isEmpty()) 0 else this.lessons.size + 1

        val lessons = ArrayList<ScheduleEventUiModel>(assumedSize)
        var previousLesson: LessonEvent? = null

        for (lesson in this.lessons) {
            if (previousLesson != null) {
                val prevEndTime = previousLesson.end.zonedTime()
                val curStartTime = lesson.start.zonedTime()
                val timeWindowInMinutes = curStartTime.untilMinutes(prevEndTime)

                if (timeWindowInMinutes >= WINDOW_THRESHOLD_MINUTES) {
                    lessons.add(
                        ScheduleEventUiModel.Window(
                            timeFrom = prevEndTime,
                            timeTo = curStartTime,
                            totalMinutes = timeWindowInMinutes,
                        ),
                    )
                }
            }
            lessons.add(ScheduleEventUiModel.Lesson(lesson))
            previousLesson = lesson
        }

        return ScheduleDayUiModel(
            isToday = this.isToday,
            date = this.date,
            lessons = lessons,
        )
    }

    companion object {
        private const val WINDOW_THRESHOLD_MINUTES = 20
    }
}
