package io.edugma.data.schedule.repository

import io.edugma.data.base.consts.CacheConst
import io.edugma.data.base.consts.PrefConst
import io.edugma.data.base.local.*
import io.edugma.data.base.model.map
import io.edugma.data.base.store.StoreImpl
import io.edugma.data.schedule.api.ScheduleService
import io.edugma.data.schedule.api.ScheduleSourcesService
import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.data.schedule.model.toModel
import io.edugma.domain.base.utils.Lce
import io.edugma.domain.base.utils.map
import io.edugma.domain.schedule.model.compact.CompactLessonFeatures
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.compact.ScheduleInfo
import io.edugma.domain.schedule.model.group.Group
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDateTime
import io.edugma.domain.schedule.model.lesson.LessonDateTimes
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.lesson_subject.LessonSubject
import io.edugma.domain.schedule.model.lesson_type.LessonType
import io.edugma.domain.schedule.model.place.Place
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.source.ScheduleSourceFull
import io.edugma.domain.schedule.model.source.ScheduleSources
import io.edugma.domain.schedule.model.teacher.Teacher
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.util.*
import kotlin.time.Duration.Companion.days

class ScheduleRepositoryImpl(
    private val scheduleService: ScheduleService,
    private val cachedDS: CacheVersionLocalDS
) : ScheduleRepository {
    override fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>> {
        return scheduleStore.get(source, false)
    }

    private val scheduleStore = StoreImpl<ScheduleSource, CompactSchedule>(
        fetcher = { key -> scheduleService.getCompactSchedule(key.type.name.lowercase(), key.key) },
        reader = { key ->
            cachedDS.getFlow<ScheduleDao>(CacheConst.Schedule + key, expireAt)
                .map { it.map { it?.days } }
        },
        writer = { key, data ->
            flowOf(cachedDS.save(ScheduleDao.from(key, data), CacheConst.Schedule + key))
        },
        expireAt = 1.days
    )

    override fun getSchedule(source: ScheduleSource, forceUpdate: Boolean) =
        scheduleStore.get(source, forceUpdate)
            .map { it.map { it?.toModel() ?: emptyList() } }
            .flowOn(Dispatchers.IO)

    override fun getLessonsReview(source: ScheduleSource) =
        scheduleService.getLessonsReview(source.type.name.lowercase(), source.key)
            .flowOn(Dispatchers.IO)
}