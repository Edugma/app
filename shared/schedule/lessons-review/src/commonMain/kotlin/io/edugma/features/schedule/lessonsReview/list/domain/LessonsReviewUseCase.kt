package io.edugma.features.schedule.lessonsReview.list.domain

import io.edugma.core.api.utils.LceFlow
import io.edugma.core.api.utils.map
import io.edugma.features.schedule.domain.model.compact.zonedDateTime
import io.edugma.features.schedule.domain.usecase.GetCurrentScheduleUseCase

class LessonsReviewUseCase(
    private val getCurrentScheduleUseCase: GetCurrentScheduleUseCase,
) {
    suspend fun getLessonsReview(): LceFlow<List<LessonReviewUiState>> {
        val scheduleFlow = getCurrentScheduleUseCase()

        return scheduleFlow.map { schedule ->
            val lessonsBySubjectId = schedule.lessons.groupBy { it.subjectId }

            lessonsBySubjectId.map { (subject, events) ->
                LessonReviewUiState(
                    subject = schedule.getSubject(subject),
                    events = events.flatMap {
                        it.toLessonReviewEvent()
                    }.groupBy { it.first }.map { (period, groupedEvents) ->
                        LessonReviewEventsByPeriod(
                            period = period,
                            events = groupedEvents.map { it.second }.sortedBy {
                                it.event.start.zonedDateTime()
                            },
                        )
                    },
                )
            }
        }
    }
}
