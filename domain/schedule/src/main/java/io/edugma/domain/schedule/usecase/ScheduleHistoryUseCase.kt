package io.edugma.domain.schedule.usecase

import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.repository.ScheduleRepository
import io.edugma.domain.schedule.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.transformLatest
import java.time.LocalDate
import kotlin.math.max

class ScheduleHistoryUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository
) {
    fun getHistory() =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest {
                val source = it.getOrNull()
                if (source == null) {
                    emit(Result.failure(Exception()))
                } else {
                    emitAll(repository.getHistory(ScheduleSource(source.type, source.key)))
                }
            }

    suspend fun getChanges2(): List<ScheduleDayChange> {
        val new = getHistory().first().getOrNull()
        val old = repository.getHistory(ScheduleSource(ScheduleSources.Group, "191-721")).first().getOrNull()

        return getChanges(old!!.toList().first().second, new!!.toList().first().second)

    }

    private fun getChanges(
        oldSchedule: List<ScheduleDay>,
        newSchedule: List<ScheduleDay>
    ): List<ScheduleDayChange> {
        val dateToLessons = mutableMapOf<LocalDate, MutableList<List<LessonsByTime>>>()

        oldSchedule.forEach {
            val list = dateToLessons.getOrPut(it.date) { ArrayList(2) }
            list.add(it.lessons)
        }

        newSchedule.forEach {
            val list = dateToLessons.getOrPut(it.date) { ArrayList(2) }
            list.add(it.lessons)
        }

        val lessons = dateToLessons.map { (date, pair) ->
            val oldScheduleDay = pair.getOrElse(0) { emptyList() }
            val newScheduleDay = pair.getOrElse(1) { emptyList() }

            val changes = getDayChanges(
                oldDay = oldScheduleDay,
                newDay = newScheduleDay
            )

            ScheduleDayChange(
                date = date,
                lessons = changes
            )
        }.filter { it.lessons.isNotEmpty() }
            .sortedBy { it.date }

        return lessons
    }

    private fun getDayChanges(
        oldDay: List<LessonsByTime>,
        newDay: List<LessonsByTime>
    ): List<LessonsByTimeChange> {
        val timeToLessons = mutableMapOf<LessonTime, MutableList<List<Lesson>>>()

        oldDay.forEach {
            val list = timeToLessons.getOrPut(it.time) { ArrayList(2) }
            list.add(it.lessons)
        }

        newDay.forEach {
            val list = timeToLessons.getOrPut(it.time) { ArrayList(2) }
            list.add(it.lessons)
        }

        val lessons = timeToLessons.map { (time, pair) ->
            val old = pair.getOrElse(0) { emptyList() }
            val new = pair.getOrElse(1) { emptyList() }

            LessonsByTimeChange(
                time = time,
                lessons = getPlaceChanges(old, new)
            )
        }.filter { it.lessons.isNotEmpty() }
            .sortedBy { it.time }

        return lessons
    }

    private fun getPlaceChanges(
        oldLessonsByTime: List<Lesson>,
        newLessonsByTime: List<Lesson>
    ): List<LessonChange> {
        val groupedBySubject = mutableMapOf<LessonSubject, MutableList<MutableList<Lesson>>>()

        oldLessonsByTime.forEach { lesson ->
            val list = groupedBySubject.getOrPut(lesson.subject) { MutableList(2) { mutableListOf() } }
            list[0].add(lesson)
        }

        newLessonsByTime.forEach { lesson ->
            val list = groupedBySubject.getOrPut(lesson.subject) { MutableList(2) { mutableListOf() } }
            // Skip lesson if it in list[0] and remove it from list[0]
            if (list[0].remove(lesson).not()) {
                list[1].add(lesson)
            }
        }

        val lessons = groupedBySubject.map { (subject, pair) ->
            val oldList = pair.getOrElse(0) { emptyList() }
            val newList = pair.getOrElse(1) { emptyList() }

            val size = max(oldList.size, newList.size)

            (0 until size).mapNotNull { index ->
                getLessonChanges(
                    oldLesson = oldList.getOrNull(index),
                    newLesson = newList.getOrNull(index)
                )
            }
        }

        return lessons.flatten()
    }

    private fun getLessonChanges(
        oldLesson: Lesson?,
        newLesson: Lesson?
    ): LessonChange? {
        if (oldLesson == newLesson) return null

        return when {
            oldLesson != null && newLesson != null ->
                LessonChange.Modified(old = oldLesson, new = newLesson)
            oldLesson != null -> LessonChange.Removed(oldLesson)
            newLesson != null -> LessonChange.Added(newLesson)
            else -> null
        }
    }
}

sealed class LessonChange {
    data class Modified(
        val old: Lesson,
        val new: Lesson
    ) : LessonChange()

    data class Added(
        val new: Lesson
    ) : LessonChange()

    data class Removed(
        val old: Lesson
    ) : LessonChange()
}


data class ScheduleDayChange(
    val date: LocalDate,
    val lessons: List<LessonsByTimeChange>
)

data class LessonsByTimeChange(
    val time: LessonTime,
    val lessons: List<LessonChange>
)

