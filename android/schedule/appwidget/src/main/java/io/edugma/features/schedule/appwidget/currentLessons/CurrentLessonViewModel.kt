package io.edugma.features.schedule.appwidget.currentLessons

import com.benasher44.uuid.uuid4
import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.domain.base.utils.nowLocalDate
import io.edugma.domain.base.utils.nowLocalTime
import io.edugma.domain.base.utils.untilSeconds
import io.edugma.features.schedule.domain.model.group.Group
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.lessonSubject.LessonSubject
import io.edugma.features.schedule.domain.model.lessonType.LessonType
import io.edugma.features.schedule.domain.model.place.Place
import io.edugma.features.schedule.domain.model.place.PlaceType
import io.edugma.features.schedule.domain.model.teacher.Teacher
import io.edugma.features.schedule.domain.usecase.ScheduleUseCase
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
        id = uuid4().toString(),
        title = "Лекция",
        isImportant = false,
    ),
    LessonType(
        id = uuid4().toString(),
        title = "Лабораторная работа",
        isImportant = false,
    ),
    LessonType(
        id = uuid4().toString(),
        title = "Экзамен",
        isImportant = true,
    ),
)

val subjects = listOf(
    LessonSubject(
        id = uuid4().toString(),
        title = "Физика",
    ),
    LessonSubject(
        id = uuid4().toString(),
        title = "Управление информационными ресурсами обработки цифрового контента",
    ),
    LessonSubject(
        id = uuid4().toString(),
        title = "Информационные технологии",
    ),
)

val teachers = listOf(
    Teacher(
        id = uuid4().toString(),
        name = "Рудяк Юрий Владимирович",
        description = "",
    ),
    Teacher(
        id = uuid4().toString(),
        name = "Винокур Алексей Иосифович",
        description = "",
    ),
    Teacher(
        id = uuid4().toString(),
        name = "Меньшикова Наталия Павловна",
        description = "",
    ),
)

val groups = listOf(
    Group(
        id = uuid4().toString(),
        title = "181-721",
        description = "",
    ),
    Group(
        id = uuid4().toString(),
        title = "181-722",
        description = "",
    ),
    Group(
        id = uuid4().toString(),
        title = "181-723",
        description = "",
    ),
)

val places = listOf(
    Place(
        id = uuid4().toString(),
        title = "Пр2303",
        type = PlaceType.Building,
        description = "",
    ),
    Place(
        id = uuid4().toString(),
        title = "Пр Вц 3 (2553)",
        type = PlaceType.Building,
        description = "",
    ),
    Place(
        id = uuid4().toString(),
        title = "Webinar",
        type = PlaceType.Online,
        description = "",
    ),
)
