package com.edugma.data.schedule.repository

import com.edugma.data.schedule.api.ScheduleService
import com.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import com.edugma.features.schedule.domain.model.group.GroupInfo
import com.edugma.features.schedule.domain.model.teacher.TeacherInfo
import com.edugma.features.schedule.domain.repository.ScheduleInfoRepository

class ScheduleInfoRepositoryImpl(
    private val scheduleService: ScheduleService,
) : ScheduleInfoRepository {
    override suspend fun getTeacherInfo(id: String): TeacherInfo {
        return scheduleService.getTeacherInfo(id)
    }

    override suspend fun getGroupInfo(id: String): GroupInfo {
        return scheduleService.getGroupInfo(id)
    }

    override suspend fun getPlaceInfo(id: String): CompactPlaceInfo {
        return scheduleService.getPlaceInfo(id)
    }
}
