package io.edugma.data.schedule.repository

import io.edugma.core.api.utils.UUID
import io.edugma.core.api.utils.nowLocalDate
import io.edugma.features.schedule.domain.model.StudentDirection
import io.edugma.features.schedule.domain.model.StudentFaculty
import io.edugma.features.schedule.domain.model.compact.CompactLessonAndTimes
import io.edugma.features.schedule.domain.model.compact.CompactLessonFeatures
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.compact.ScheduleInfo
import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.lesson.LessonDateTime
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubjectInfo
import io.edugma.features.schedule.domain.model.lessonType.LessonTypeInfo
import io.edugma.features.schedule.domain.model.place.PlaceInfo
import io.edugma.features.schedule.domain.model.place.PlaceType
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalTime
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
            info = ScheduleInfo(
                typesInfo = getTypes(),
                subjectsInfo = getSubjects(),
                teachersInfo = getTeachers(),
                groupsInfo = getGroups(),
                placesInfo = getPlaces(),
            ),
        )
    }

    private fun getLessons() = CompactLessonAndTimes(
        lesson = CompactLessonFeatures(
            typeId = "1",
            subjectId = "1",
            teachersId = listOf("1"),
            groupsId = listOf("1"),
            placesId = listOf("1"),
        ),
        times = listOf(
            LessonDateTime(
                startDate = Clock.System.nowLocalDate().minus(DatePeriod(days = 50)),
                endDate = Clock.System.nowLocalDate().plus(DatePeriod(days = 50)),
                time = LessonTime(
                    start = LocalTime(10, 40, 0, 0),
                    end = LocalTime(12, 10, 0, 0),
                ),
            ),
        ),
    )

    private fun getPlaces() = listOf(
        PlaceInfo.Building(
            id = "1",
            title = "Пр 2351",
            type = PlaceType.Building,
            areaAlias = "Пряники",
            street = "",
            building = "null",
            floor = "null",
            auditorium = "null",
            location = null,
            description = "null",
        ),
    )

    private fun getGroups() = listOf(
        GroupInfo(
            id = "1",
            title = "224-372",
            course = 2,
            faculty = StudentFaculty(
                id = "1",
                title = "Факультет информационных технологий",
                titleShort = "ФИТ",
            ),
            direction = StudentDirection(
                id = "1",
                title = "Информационные системы и технологии",
                code = "09.04.02",
            ),
        ),
    )

    private fun getTeachers() = listOf(
        TeacherInfo(
            id = "1",
            name = "Арсентьев Дмитрий Андреевич",
            avatar = null,
            stuffType = null,
            grade = null,
            departmentParent = null,
            department = null,
            email = null,
            sex = null,
            birthday = null,
        ),
    )

    private fun getSubjects() = listOf(
        LessonSubjectInfo(
            id = "1",
            title = "Мультиплатформенная разработка мобильных приложений " +
                UUID.get().take(5),
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
