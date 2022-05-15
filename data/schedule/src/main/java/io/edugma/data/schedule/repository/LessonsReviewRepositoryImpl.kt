package io.edugma.data.schedule.repository

import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.review.LessonDates
import io.edugma.domain.schedule.model.review.LessonReviewUnit
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.review.LessonTimesReviewByType
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.repository.LessonsReviewRepository
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class LessonsReviewRepositoryImpl(
    private val scheduleRepository: ScheduleRepository
) : LessonsReviewRepository {

    override fun getLessonsReview(source: ScheduleSource) =
        flow {
            emit(kotlin.runCatching { getLessonsReview2(source) })
        }.flowOn(Dispatchers.IO)


    private suspend fun getLessonsReview2(source: ScheduleSource): List<LessonTimesReview> {
        val schedule = scheduleRepository.getRawSchedule(source).last().getOrNull()

        val lessons = schedule?.lessons ?: emptyList()
        val info = schedule?.info



        val resMap1 = mutableMapOf<String,
                MutableMap<String,
                        MutableMap<LessonDates, MutableList<LessonTime>>
                        >
                >()

        lessons.forEach { lessonDateTimes ->
            lessonDateTimes.times.forEach { lessonDateTime ->
                val lessonDates = LessonDates(lessonDateTime.startDate, lessonDateTime.endDate)

                resMap1
                    .getOrPut(lessonDateTimes.lesson.subjectId) { mutableMapOf() }
                    .getOrPut(lessonDateTimes.lesson.typeId) { mutableMapOf() }
                    .getOrPut(lessonDates) { mutableListOf() }
                    .add(lessonDateTime.time)
            }
        }



        val resMap = resMap1.mapValues {
            it.value.mapValues {
                it.value.toList()
                    .groupBy { it.first.start.dayOfWeek!! }
                    .mapValues {
                        it.value.groupBy { it.second }
                            .mapValues { it.value.map { it.first } }
                    }

            }
        }


        val resList = resMap.map { (subjectId, typeToDays) ->
            val subject = info?.subjectsInfo?.firstOrNull { it.id == subjectId }
                ?.let { LessonSubject.from(it) }

            LessonTimesReview(
                subject = subject!!,
                days = typeToDays.map { (typeId, mapOfDays) ->
                    val type = info?.typesInfo?.firstOrNull { it.id == typeId }
                        ?.let { LessonType.from(it) }

                    LessonTimesReviewByType(
                        lessonType = type!!,
                        days = mapOfDays.flatMap { (dayOfWeek, datesAndTimes) ->
                            datesAndTimes.map { (times, dates) ->
                                LessonReviewUnit(
                                    dayOfWeek = dayOfWeek,
                                    time = times.apply { sort() },
                                    dates = getLessonDates(dates)
                                )
                            }
                        }.sorted()
                    )
                }.sorted()
            )
        }.sortedBy { it.subject }

        return resList
    }

    private fun getLessonDates(dates: List<LessonDates>): List<LessonDates> {
        val sortedDates = dates
            .flatMap { generateDatesFromRange(startDate = it.start, endDate = it.end) }
            .toSortedSet()

        val resList = mutableListOf<LessonDates>()

        sortedDates.forEach { date ->
            val lastDate = resList.lastOrNull()
            if (lastDate == null ||
                lastDate.end != null && lastDate.end!!.plusDays(7L) != date) {
                resList.add(LessonDates(date, null))
            } else {
                val newLessonDates = lastDate.copy(end = date)
                resList[resList.lastIndex] = newLessonDates
            }
        }

        return resList
    }

    private fun generateDatesFromRange(startDate: LocalDate, endDate: LocalDate?) =
        sequence {
            if (endDate == null) {
                yield(startDate)
                return@sequence
            }

            var currentDay = startDate
            do {
                yield(currentDay)
                currentDay = currentDay.plusDays(7)
            } while (currentDay <= endDate)
        }
}

private data class DatesAndTimes(
    val dates: MutableList<LocalDate> = mutableListOf(),
    val times: MutableList<LessonTime> = mutableListOf()
)