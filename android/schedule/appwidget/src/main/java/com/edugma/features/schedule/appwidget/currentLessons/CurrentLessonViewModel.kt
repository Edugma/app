package com.edugma.features.schedule.appwidget.currentLessons

import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.domain.base.utils.nowLocalDate
import com.edugma.domain.base.utils.nowLocalTime
import com.edugma.domain.base.utils.untilSeconds
import com.edugma.features.schedule.domain.model.group.Group
import com.edugma.features.schedule.domain.model.lesson.Lesson
import com.edugma.features.schedule.domain.model.lesson.LessonTime
import com.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import com.edugma.features.schedule.domain.model.lessonType.LessonType
import com.edugma.features.schedule.domain.model.place.Place
import com.edugma.features.schedule.domain.model.place.PlaceType
import com.edugma.features.schedule.domain.model.teacher.Teacher
import com.edugma.features.schedule.domain.usecase.ScheduleUseCase
import kotlinx.coroutines.flow.last
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime

class CurrentLessonViewModel(
    private val useCase: ScheduleUseCase,
) : BaseViewModel<CurrentLessonState>(CurrentLessonState()) {

    suspend fun getCurrentLessons(): List<Lesson> {
        val schedule = useCase.getSchedule().last().getOrNull() ?: emptyList()
        val todayLessons = schedule.firstOrNull { it.date == Clock.System.nowLocalDate() }?.lessons
        val nowTime = Clock.System.nowLocalTime()
        val currentLessons = todayLessons
            ?.firstOrNull { nowTime in it.time }
            ?: todayLessons?.map {
                nowTime.untilSeconds(it.time.start) to it
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
                    LocalTime(10, 40),
                    LocalTime(12, 10),
                ),
                lastUpdateDateTime = Clock.System.now(),
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
    val lastUpdateDateTime: Instant? = null,
    val time: LessonTime? = null,
    val currentLesson: Lesson? = null,
    val currentLessons: List<Lesson> = emptyList(),
    val currentLessonIndex: Int = 0,
)

val types = listOf(
    LessonType(
        id = UUID.get(),
        title = "Лекция",
        isImportant = false,
    ),
    LessonType(
        id = UUID.get(),
        title = "Лабораторная работа",
        isImportant = false,
    ),
    LessonType(
        id = UUID.get(),
        title = "Экзамен",
        isImportant = true,
    ),
)

val subjects = listOf(
    LessonSubject(
        id = UUID.get(),
        title = "Физика",
    ),
    LessonSubject(
        id = UUID.get(),
        title = "Управление информационными ресурсами обработки цифрового контента",
    ),
    LessonSubject(
        id = UUID.get(),
        title = "Информационные технологии",
    ),
)

val teachers = listOf(
    Teacher(
        id = UUID.get(),
        name = "Рудяк Юрий Владимирович",
        description = "",
    ),
    Teacher(
        id = UUID.get(),
        name = "Винокур Алексей Иосифович",
        description = "",
    ),
    Teacher(
        id = UUID.get(),
        name = "Меньшикова Наталия Павловна",
        description = "",
    ),
)

val groups = listOf(
    Group(
        id = UUID.get(),
        title = "181-721",
        description = "",
    ),
    Group(
        id = UUID.get(),
        title = "181-722",
        description = "",
    ),
    Group(
        id = UUID.get(),
        title = "181-723",
        description = "",
    ),
)

val places = listOf(
    Place(
        id = UUID.get(),
        title = "Пр2303",
        type = PlaceType.Building,
        description = "",
    ),
    Place(
        id = UUID.get(),
        title = "Пр Вц 3 (2553)",
        type = PlaceType.Building,
        description = "",
    ),
    Place(
        id = UUID.get(),
        title = "Webinar",
        type = PlaceType.Online,
        description = "",
    ),
)
