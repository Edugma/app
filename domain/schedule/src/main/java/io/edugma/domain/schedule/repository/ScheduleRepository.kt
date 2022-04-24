package io.edugma.domain.schedule.repository

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.lesson.LessonDateTimes
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.place.PlaceFilters
import io.edugma.domain.schedule.model.place.PlaceInfo
import io.edugma.domain.schedule.model.review.LessonTimesReview
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>>

    fun getSources(type: ScheduleSources): Flow<Result<List<ScheduleSourceFull>>>

    suspend fun setSelectedSource(source: ScheduleSourceFull)
    fun getSelectedSource(): Flow<Result<ScheduleSourceFull?>>

    fun getSchedule(source: ScheduleSource, forceUpdate: Boolean = false): Flow<Lce<List<ScheduleDay>>>
    fun getLessonsReview(source: ScheduleSource): Flow<Result<List<LessonTimesReview>>>
    fun getSourceTypes(): Flow<Result<List<ScheduleSources>>>
}