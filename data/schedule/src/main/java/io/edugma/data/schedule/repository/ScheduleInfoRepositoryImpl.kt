package io.edugma.data.schedule.repository

import io.edugma.data.schedule.api.ScheduleInfoService
import io.edugma.domain.schedule.repository.ScheduleInfoRepository
import io.edugma.domain.schedule.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class ScheduleInfoRepositoryImpl(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleInfoService: ScheduleInfoService,
) : ScheduleInfoRepository {
    override fun getTeacherInfo(id: String) =
        scheduleInfoService.getTeacherInfo(id)
            .flowOn(Dispatchers.IO)

    override fun getGroupInfo(id: String) =
        scheduleInfoService.getGroupInfo(id)
            .flowOn(Dispatchers.IO)

    override fun getPlaceInfo(id: String) =
        scheduleInfoService.getPlaceInfo(id)
            .flowOn(Dispatchers.IO)

    override fun getLessonSubjectInfo(id: String) =
        scheduleInfoService.getSubjectInfo(id)
            .flowOn(Dispatchers.IO)

    override fun getLessonTypeInfo(id: String) =
        scheduleInfoService.getLessonTypeInfo(id)
            .flowOn(Dispatchers.IO)
}
