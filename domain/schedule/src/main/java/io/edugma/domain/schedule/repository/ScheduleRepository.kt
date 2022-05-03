package io.edugma.domain.schedule.repository

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>>
    fun getSchedule(source: ScheduleSource, forceUpdate: Boolean = false): Flow<Lce<List<ScheduleDay>>>
}