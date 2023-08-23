package io.edugma.features.schedule.domain.repository

import io.edugma.core.api.utils.Lce
import io.edugma.features.schedule.domain.model.ScheduleRecord
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.schedule.ScheduleDay
import io.edugma.features.schedule.domain.model.source.ScheduleSource
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ScheduleRepository {
    fun getRawSchedule(source: ScheduleSource): Flow<Lce<CompactSchedule?>>
    fun getSchedule(source: ScheduleSource, forceUpdate: Boolean = false): Flow<Lce<List<ScheduleDay>>>
    fun getHistory(source: ScheduleSource): Flow<List<ScheduleRecord>>
    suspend fun getHistoryRecord(source: ScheduleSource, timestamp: Instant): ScheduleRecord?
    fun getTeacher(source: ScheduleSource, id: String): Flow<TeacherInfo?>
}
