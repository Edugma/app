package io.edugma.features.schedule.domain.repository

import io.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo

interface ScheduleInfoRepository {
    suspend fun getTeacherInfo(id: String): TeacherInfo
    suspend fun getGroupInfo(id: String): GroupInfo
    suspend fun getPlaceInfo(id: String): CompactPlaceInfo
}
