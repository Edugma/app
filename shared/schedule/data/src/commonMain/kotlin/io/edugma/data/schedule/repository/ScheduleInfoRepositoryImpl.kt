package io.edugma.data.schedule.repository

import io.edugma.data.schedule.api.ScheduleService
import io.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository

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
