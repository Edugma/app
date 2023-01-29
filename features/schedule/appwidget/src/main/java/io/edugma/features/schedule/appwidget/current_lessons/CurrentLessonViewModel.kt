package io.edugma.features.schedule.appwidget.current_lessons

import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lesson_subject.LessonSubject
import io.edugma.features.schedule.domain.model.lesson_type.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.place.PlaceType
import io.edugma.features.schedule.domain.model.teacher.Teacher
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.coroutines.flow.last
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class CurrentLessonViewModel(
    private val useCase: ScheduleUseCase,
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
                places = (0..(0..3).random()).map { places.random() },
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
                    LocalTime.of(12, 10),
                ),
                lastUpdateDateTime = ZonedDateTime.now(),
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
    val currentLessonIndex: Int = 0,
)

val types = listOf(
    LessonType(
        id = UUID.randomUUID().toString(),
        title = "Лекция",
        isImportant = false,
    ),
    LessonType(
        id = UUID.randomUUID().toString(),
        title = "Лабораторная работа",
        isImportant = false,
    ),
    LessonType(
        id = UUID.randomUUID().toString(),
        title = "Экзамен",
        isImportant = true,
    ),
)

val subjects = listOf(
    LessonSubject(
        id = UUID.randomUUID().toString(),
        title = "Физика",
    ),
    LessonSubject(
        id = UUID.randomUUID().toString(),
        title = "Управление информационными ресурсами обработки цифрового контента",
    ),
    LessonSubject(
        id = UUID.randomUUID().toString(),
        title = "Информационные технологии",
    ),
)

val teachers = listOf(
    Teacher(
        id = UUID.randomUUID().toString(),
        name = "Рудяк Юрий Владимирович",
        description = "",
    ),
    Teacher(
        id = UUID.randomUUID().toString(),
        name = "Винокур Алексей Иосифович",
        description = "",
    ),
    Teacher(
        id = UUID.randomUUID().toString(),
        name = "Меньшикова Наталия Павловна",
        description = "",
    ),
)

val groups = listOf(
    Group(
        id = UUID.randomUUID().toString(),
        title = "181-721",
        description = "",
    ),
    Group(
        id = UUID.randomUUID().toString(),
        title = "181-722",
        description = "",
    ),
    Group(
        id = UUID.randomUUID().toString(),
        title = "181-723",
        description = "",
    ),
)

val places = listOf(
    Place(
        id = UUID.randomUUID().toString(),
        title = "Пр2303",
        type = PlaceType.Building,
        description = "",
    ),
    Place(
        id = UUID.randomUUID().toString(),
        title = "Пр Вц 3 (2553)",
        type = PlaceType.Building,
        description = "",
    ),
    Place(
        id = UUID.randomUUID().toString(),
        title = "Webinar",
        type = PlaceType.Online,
        description = "",
    ),
)
