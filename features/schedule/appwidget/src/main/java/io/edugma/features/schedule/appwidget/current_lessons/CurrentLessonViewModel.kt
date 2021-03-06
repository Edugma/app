package io.edugma.features.schedule.appwidget.current_lessons

import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.PlaceType
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.domain.schedule.usecase.ScheduleUseCase
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.flow.last
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class CurrentLessonViewModel(
    private val useCase: ScheduleUseCase
) : BaseViewModel<CurrentLessonState>(CurrentLessonState()) {

    suspend fun getCurrentLessons(): List<Lesson> {
        val schedule = useCase.getSchedule().last().getOrNull() ?: emptyList()
        val todayLessons = schedule.firstOrNull { it.date == LocalDate.now() }?.lessons
        val nowTime = LocalTime.now()
        val currentLessons = todayLessons
            ?.firstOrNull { nowTime in it.time }
            ?: todayLessons?.map {
            nowTime.until(it.time.start, ChronoUnit.SECONDS) to it
        }?.filter { it.first >= 0 }
            ?.minByOrNull { it.first }?.second

        throw NotImplementedError()
    }

    fun setLessons() {
        val lessons = (0..(0..4).random()).map {
            Lesson(
                type = types.random(),
                subject = subjects.random(),
                teachers = (0..(0..3).random()).map { teachers.random() },
                groups = (0..(0..5).random()).map { groups.random() },
                places = (0..(0..3).random()).map { places.random() }
            )
        }

        mutateState {
            val index = if (lessons.isNotEmpty()) {
                state.currentLessonIndex
                    .coerceIn(lessons.indices)
            } else {
                0
            }

            state = state.copy(
                currentLessons = lessons,
                currentLessonIndex = index,
                currentLesson = lessons.getOrNull(index),
                time = LessonTime(
                    LocalTime.of(10, 40),
                    LocalTime.of(12, 10)
                ),
                lastUpdateDateTime = ZonedDateTime.now()
            )
        }
    }

    fun onLessonIndex(index: Int) {
        mutateState {
            state = state.copy(currentLessonIndex = index)
        }

        setLessons()
    }
}

data class CurrentLessonState(
    val lastUpdateDateTime: ZonedDateTime? = null,
    val time: LessonTime? = null,
    val currentLesson: Lesson? = null,
    val currentLessons: List<Lesson> = emptyList(),
    val currentLessonIndex: Int = 0
)

val types = listOf(
    LessonType(
        id = UUID.randomUUID().toString(),
        title = "????????????",
        isImportant = false
    ),
    LessonType(
        id = UUID.randomUUID().toString(),
        title = "???????????????????????? ????????????",
        isImportant = false
    ),
    LessonType(
        id = UUID.randomUUID().toString(),
        title = "??????????????",
        isImportant = true
    ),
)

val subjects = listOf(
    LessonSubject(
        id = UUID.randomUUID().toString(),
        title = "????????????",
    ),
    LessonSubject(
        id = UUID.randomUUID().toString(),
        title = "???????????????????? ?????????????????????????????? ?????????????????? ?????????????????? ?????????????????? ????????????????",
    ),
    LessonSubject(
        id = UUID.randomUUID().toString(),
        title = "???????????????????????????? ????????????????????",
    ),
)

val teachers = listOf(
    Teacher(
        id = UUID.randomUUID().toString(),
        name = "?????????? ???????? ????????????????????????",
    ),
    Teacher(
        id = UUID.randomUUID().toString(),
        name = "?????????????? ?????????????? ??????????????????",
    ),
    Teacher(
        id = UUID.randomUUID().toString(),
        name = "???????????????????? ?????????????? ????????????????",
    )
)

val groups = listOf(
    Group(
        id = UUID.randomUUID().toString(),
        title = "181-721",
    ),
    Group(
        id = UUID.randomUUID().toString(),
        title = "181-722",
    ),
    Group(
        id = UUID.randomUUID().toString(),
        title = "181-723",
    ),
)

val places = listOf(
    Place(
        id = UUID.randomUUID().toString(),
        title = "????2303",
        type = PlaceType.Building
    ),
    Place(
        id = UUID.randomUUID().toString(),
        title = "???? ???? 3 (2553)",
        type = PlaceType.Building
    ),
    Place(
        id = UUID.randomUUID().toString(),
        title = "Webinar",
        type = PlaceType.Online
    ),
)