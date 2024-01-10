package io.edugma.features.schedule.domain.usecase

import io.edugma.core.api.hash.Hash
import io.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.compact.toModel
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import io.edugma.features.schedule.domain.repository.ScheduleSourcesRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
import kotlinx.datetime.Instant

class ScheduleHistoryUseCase(
    private val repository: ScheduleRepository,
    private val scheduleSourcesRepository: ScheduleSourcesRepository,
    private val scheduleRepository: ScheduleRepository,
) {
    fun getHistory() =
        scheduleSourcesRepository.getSelectedSource()
            .transformLatest { source ->
                if (source == null) {
                    error("")
                } else {
                    emitAll(repository.getHistory(ScheduleSource(source.type, source.id)))
                }
            }.map { it.asReversed() }

    suspend fun getChanges(
        firstScheduleTimestamp: Instant,
        secondScheduleTimestamp: Instant,
    ): List<LessonChange> {
        val source = scheduleSourcesRepository.getSelectedSource().first() ?: return emptyList()
        val scheduleSource = ScheduleSource(source.type, source.id)
        val firstSchedule = scheduleRepository.getHistoryRecord(
            source = scheduleSource,
            timestamp = firstScheduleTimestamp,
        ) ?: return emptyList()
        val secondSchedule = scheduleRepository.getHistoryRecord(
            source = scheduleSource,
            timestamp = secondScheduleTimestamp,
        ) ?: return emptyList()

        return calculateChanges(firstSchedule.schedule, secondSchedule.schedule)
    }

    private fun CompactLessonEvent.getHash(): UInt {
        return Hash.hash(this.toString())
    }

    private fun calculateChanges(
        oldSchedule: CompactSchedule,
        newSchedule: CompactSchedule,
    ): List<LessonChange> {
        val oldUniqueEvents = mutableMapOf<UInt, CompactLessonEvent>()
        val newUniqueEvents = mutableMapOf<UInt, CompactLessonEvent>()
        val resultChanges = mutableListOf<LessonChange>()

        oldSchedule.lessons.forEach { event ->
            oldUniqueEvents[event.getHash()] = event
        }

        newSchedule.lessons.forEach { event ->
            val newEventHash = event.getHash()
            if (newEventHash in oldUniqueEvents) {
                // Remove events that not changed
                oldUniqueEvents.remove(newEventHash)
            } else {
                newUniqueEvents[event.getHash()] = event
                resultChanges.add(LessonChange.Added(event.toModel(newSchedule)))
            }
        }

        oldUniqueEvents.forEach { (oldEventHash, event) ->
            resultChanges.add(LessonChange.Removed(event.toModel(oldSchedule)))
        }

        return resultChanges
    }

//    private fun getPlaceChanges(
//        oldLessonsByTime: List<CompactLessonEvent>,
//        newLessonsByTime: List<CompactLessonEvent>,
//    ): List<LessonChange> {
//        val groupedBySubject = mutableMapOf<LessonSubject, MutableList<MutableList<CompactLessonEvent>>>()
//
//        oldLessonsByTime.forEach { lesson ->
//            val list = groupedBySubject.getOrPut(lesson.subject) { MutableList(2) { mutableListOf() } }
//            list[0].add(lesson)
//        }
//
//        newLessonsByTime.forEach { lesson ->
//            val list = groupedBySubject.getOrPut(lesson.subject) { MutableList(2) { mutableListOf() } }
//            // Skip lesson if it in list[0] and remove it from list[0]
//            if (list[0].remove(lesson).not()) {
//                list[1].add(lesson)
//            }
//        }
//
//        val lessons = groupedBySubject.map { (subject, pair) ->
//            val oldList = pair.getOrElse(0) { emptyList() }
//            val newList = pair.getOrElse(1) { emptyList() }
//
//            val size = max(oldList.size, newList.size)
//
//            (0 until size).mapNotNull { index ->
//                getLessonChanges(
//                    oldLesson = oldList.getOrNull(index),
//                    newLesson = newList.getOrNull(index),
//                )
//            }
//        }
//
//        return lessons.flatten()
//    }

//    private fun getLessonChanges(
//        oldLesson: LessonEvent?,
//        newLesson: LessonEvent?,
//    ): LessonChange? {
//        if (oldLesson == newLesson) return null
//
//        return when {
//            oldLesson != null && newLesson != null ->
//                LessonChange.Modified(old = oldLesson, new = newLesson)
//            oldLesson != null -> LessonChange.Removed(oldLesson)
//            newLesson != null -> LessonChange.Added(newLesson)
//            else -> null
//        }
//    }
}

sealed class LessonChange {
    data class Modified(
        val old: LessonEvent,
        val new: LessonEvent,
    ) : LessonChange()

    data class Added(
        val new: LessonEvent,
    ) : LessonChange()

    data class Removed(
        val old: LessonEvent,
    ) : LessonChange()
}
