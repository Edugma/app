package io.edugma.data.schedule.repository

import io.edugma.data.schedule.api.ScheduleInfoService
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import io.edugma.features.schedule.domain.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ScheduleInfoRepositoryImpl(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleInfoService: ScheduleInfoService,
) : ScheduleInfoRepository {
    override fun getTeacherInfo(id: String) =
        flow { emit(scheduleInfoService.getTeacherInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getGroupInfo(id: String) =
        flow { emit(scheduleInfoService.getGroupInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getPlaceInfo(id: String) =
        flow { emit(scheduleInfoService.getPlaceInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getLessonSubjectInfo(id: String) =
        flow { emit(scheduleInfoService.getSubjectInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getLessonTypeInfo(id: String) =
        flow { emit(scheduleInfoService.getLessonTypeInfo(id)) }
            .flowOn(Dispatchers.IO)
}
