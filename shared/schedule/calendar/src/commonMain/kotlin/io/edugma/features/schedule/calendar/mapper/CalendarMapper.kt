package io.edugma.features.schedule.calendar.mapper

import androidx.compose.ui.text.buildAnnotatedString
import io.edugma.features.schedule.calendar.model.CalendarLessonPlaceVO
import io.edugma.features.schedule.calendar.model.CalendarLessonVO
import io.edugma.features.schedule.calendar.model.CalendarScheduleVO
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import kotlinx.datetime.plus

class CalendarMapper {
    fun map(scheduleCalendar: ScheduleCalendar): List<CalendarScheduleVO> {
        return scheduleCalendar.toCalendarUiModel()
    }

    private fun ScheduleCalendar.toCalendarUiModel(): List<CalendarScheduleVO> {
        // TODO
        TODO()
//        if (isEmpty()) return emptyList()
//
//        var res = asSequence()
//
//        val firstItem = first()
//        val floorMonday = firstItem.date.getFloorMonday()
//
//        if (floorMonday != firstItem.date) {
//            val daysToAdd = floorMonday.daysUntil(firstItem.date)
//            res = sequence {
//                (0 until daysToAdd).forEach {
//                    val date = floorMonday.plus(it, DateTimeUnit.DAY)
//                    yield(
//                        ScheduleDay(
//                            date = date,
//                            lessons = emptyList(),
//                        ),
//                    )
//                }
//            } + res
//        }
//
//        val lastItem = last()
//        val ceilSunday = lastItem.date.getCeilSunday()
//
//        if (ceilSunday != lastItem.date) {
//            val daysToAdd = lastItem.date.daysUntil(ceilSunday)
//            res += sequence {
//                (1..daysToAdd).forEach {
//                    val date = lastItem.date.plus(it, DateTimeUnit.DAY)
//                    yield(
//                        ScheduleDay(
//                            date = date,
//                            lessons = emptyList(),
//                        ),
//                    )
//                }
//            }
//        }
//
//        return res.windowed(7, 7)
//            .mapIndexed { index, list ->
//                CalendarScheduleVO(
//                    weekNumber = index,
//                    weekSchedule = list.map { scheduleDay ->
//                        CalendarDayVO(
//                            dayTitle = scheduleDay.date.format("EEE, d MMM").uppercase(),
//                            date = scheduleDay.date,
//                            lessons = scheduleDay.lessons.map { lessonsByTime ->
//                                mapToLessonPlace(lessonsByTime)
//                            },
//                        )
//                    },
//                )
//            }.toList()
    }

    private fun mapToLessonPlace(lesson: LessonEvent) =
        CalendarLessonPlaceVO(
            time = lesson.mapTime(),
            lessons = listOf(
                CalendarLessonVO(
                    title = mapLesson(
                        lesson = lesson,
                        isLast = true,
                    ),
                    importance = lesson.importance,
                ),
            ),
        )

    private fun mapLesson(
        lesson: LessonEvent,
        isLast: Boolean,
    ) = buildAnnotatedString {
        append(cutTitle(lesson.subject.title))
//        append(" (")
//        if (false && lesson.type.isImportant) {
//            //pushStyle(SpanStyle(color = colorError))
//            append(getShortType(lesson.type.title))
//            pop()
//        } else {
//            append(getShortType(lesson.type.title))
//        }
//        append(")")
        if (!isLast) {
            append("\n")
        }
    }.toString()

    private fun LessonEvent.mapTime(): String {
        // TODO TimeZone
        return "•${start.dateTime.time} - ${end.dateTime.time}"
    }
}

private const val minCriticalTitleLength = 10
private const val minCriticalWordLength = 5

private const val vowels = "аеёиоуыэьъюя"
private const val specChars = "ьъ"

// TODO: Fix ьъ
fun cutTitle(title: String): String {
    if (title.length <= minCriticalTitleLength) {
        return title
    }
    val words = title.split(' ').filter { it.isNotEmpty() }
    if (words.size == 1) {
        return words.first()
    }

    return words.joinToString(" ") { it }
}

// private fun cutWord(word: String): String {
//    if (word.length <= minCriticalWordLength) {
//        return word
//    }
//
//    val vowelIndex = word.indexOfFirst { vowels.contains(it, ignoreCase = true) }
//    if (vowelIndex == -1) {
//        return word
//    }
//    for (i in vowelIndex + 1 until word.length) {
//        // TODO: Fix for spec chars
//        if (vowels.contains(word[i], ignoreCase = true) && !specChars.contains(word[i], ignoreCase = true)) {
//            // if two vowels are near or shorted word will be too short
//            if (i == vowelIndex + 1 || i < 3) {
//                continue
//            }
//            return word.substring(0, i) + '.'
//        }
//    }
//    return word
// }
