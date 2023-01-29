package io.edugma.features.schedule.domain.repository

import io.edugma.domain.base.utils.Lce
import io.edugma.domain.schedule.model.compact.CompactSchedule
import io.edugma.domain.schedule.model.schedule.ScheduleDay
import io.edugma.domain.schedule.model.source.ScheduleSource
import io.edugma.domain.schedule.model.teacher.TeacherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ScheduleRepository {
    fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>>
    fun getSchedule(source: ScheduleSource, forceUpdate: Boolean = false): Flow<Lce<List<ScheduleDay>>>
    fun getHistory(source: ScheduleSource): Flow<Result<Map<Instant, List<ScheduleDay>>>>
    fun getTeacher(source: ScheduleSource, id: String): Flow<TeacherInfo?>
}
