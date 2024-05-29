package com.edugma.data.schedule.repository

import com.edugma.core.api.utils.UUID
import com.edugma.core.api.utils.nowLocalDate
import com.edugma.features.schedule.domain.model.attentdee.AttendeeInfo
import com.edugma.features.schedule.domain.model.attentdee.AttendeeType
import com.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import com.edugma.features.schedule.domain.model.compact.CompactLessonSubjectInfo
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.compact.CompactSchedule
import com.edugma.features.schedule.domain.model.compact.Importance
import com.edugma.features.schedule.domain.model.compact.LessonDateTime
import com.edugma.features.schedule.domain.model.lessonType.LessonTypeInfo
import com.edugma.features.schedule.domain.model.rrule.Frequency
import com.edugma.features.schedule.domain.model.rrule.RRule
import com.edugma.features.schedule.domain.model.rrule.Weekday
import com.edugma.features.schedule.domain.model.rrule.WeekdayNum
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.time.Duration.Companion.seconds

class ScheduleMockRepository {

    suspend fun getSuspendMockSchedule(): Result<CompactSchedule> {
        delay(10.seconds)
        return Result.success(getMockSchedule())
    }
    fun getMockSchedule(): CompactSchedule {
        return CompactSchedule(
            lessons = listOf(getLessons()),
            subjects = getSubjects(),
            attendees = getTeachers() + getGroups(),
            places = getPlaces(),
        )
    }

    private fun getLessons() = CompactLessonEvent(
        id = "",
        tags = listOf("1"),
        subjectId = "1",
        attendeesId = listOf("t1", "g1"),
        placesId = listOf("1"),
        start = LessonDateTime(
            dateTime = Clock.System.nowLocalDate().minus(DatePeriod(days = 50))
                .atTime(LocalTime(10, 40, 0, 0)),
            timeZone = TimeZone.currentSystemDefault(),
        ),
        end = LessonDateTime(
            dateTime = Clock.System.nowLocalDate().plus(DatePeriod(days = 50))
                .atTime(LocalTime(12, 10, 0, 0)),
            timeZone = TimeZone.currentSystemDefault(),
        ),
        importance = Importance.Normal,
        recurrence = listOf(
            RRule(
                frequency = Frequency.Weekly,
                byWeekday = listOf(WeekdayNum(0, Weekday.Wednesday)),
            ),
        ),
    )

    private fun getPlaces() = listOf(
        CompactPlaceInfo.Building(
            id = "1",
            title = "Пр 2351",
            coordinates = null,
            description = "Пряники",
        ),
    )

    private fun getGroups(): List<AttendeeInfo> = listOf(
        AttendeeInfo(
            id = "1",
            type = AttendeeType.Group,
            name = "224-372",
            description = "Факультет информационных технологий",
            avatar = null,
        ),
    )

    private fun getTeachers(): List<AttendeeInfo> = listOf(
        AttendeeInfo(
            id = "1",
            type = AttendeeType.Teacher,
            name = "Арсентьев Дмитрий Андреевич",
            description = "Факультет информационных технологий",
            avatar = null,
        ),
    )

    private fun getSubjects() = listOf(
        CompactLessonSubjectInfo(
            id = "1",
            title = "Мультиплатформенная разработка мобильных приложений " +
                UUID.get().take(5),
            description = null,
            type = null,
        ),
    )

    private fun getTypes(): List<LessonTypeInfo> {
        return listOf(
            LessonTypeInfo(
                id = "1",
                title = "Лекция",
                shortTitle = "Лекция",
                description = "",
                isImportant = false,
            ),
            LessonTypeInfo(
                id = "2",
                title = "Экзамен",
                shortTitle = "Экзамен",
                description = "",
                isImportant = true,
            ),
        )
    }
}
