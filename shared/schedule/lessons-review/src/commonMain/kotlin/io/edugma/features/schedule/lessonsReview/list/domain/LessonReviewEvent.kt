package io.edugma.features.schedule.lessonsReview.list.domain

import androidx.compose.runtime.Immutable
import io.edugma.features.schedule.domain.model.compact.CompactLessonEvent
import io.edugma.features.schedule.domain.model.rrule.RRule

@Immutable
sealed interface LessonReviewEvent {
    val event: CompactLessonEvent

    data class OneTime(
        override val event: CompactLessonEvent,
    ) : LessonReviewEvent

    data class Repeated(
        override val event: CompactLessonEvent,
        val rrule: RRule,
    ) : LessonReviewEvent
}

fun CompactLessonEvent.toLessonReviewEvent(): List<Pair<LessonReviewPeriod, LessonReviewEvent>> {
    return if (recurrence.isEmpty()) {
        listOf(
            LessonReviewPeriod.OneTime to LessonReviewEvent.OneTime(
                event = this,
            ),
        )
    } else {
        recurrence.map { rrule ->
            val period = LessonReviewPeriod.Repeated(
                frequency = rrule.frequency,
                interval = rrule.interval,
            )
            val event = LessonReviewEvent.Repeated(
                event = this,
                rrule = rrule,
            )
            period to event
        }
    }
}
