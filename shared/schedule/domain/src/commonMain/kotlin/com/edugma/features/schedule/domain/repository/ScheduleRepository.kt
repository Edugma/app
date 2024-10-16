package com.edugma.features.schedule.domain.repository

import com.edugma.core.api.utils.LceFlow
import com.edugma.features.schedule.domain.model.ScheduleRecord
import com.edugma.features.schedule.domain.model.compact.CompactSchedule
import com.edugma.features.schedule.domain.model.schedule.ScheduleCalendar
import com.edugma.features.schedule.domain.model.source.ScheduleSource
import com.edugma.features.schedule.domain.model.teacher.TeacherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ScheduleRepository {
    fun getRawSchedule(
        source: ScheduleSource,
        forceUpdate: Boolean = false,
    ): LceFlow<CompactSchedule>
    fun getSchedule(source: ScheduleSource, forceUpdate: Boolean = false): LceFlow<ScheduleCalendar>
    fun getHistory(source: ScheduleSource): Flow<List<ScheduleRecord>>
    suspend fun getHistoryRecord(source: ScheduleSource, timestamp: Instant): ScheduleRecord?
    fun getTeacher(source: ScheduleSource, id: String): LceFlow<TeacherInfo?>
    suspend fun clearAll()
}
