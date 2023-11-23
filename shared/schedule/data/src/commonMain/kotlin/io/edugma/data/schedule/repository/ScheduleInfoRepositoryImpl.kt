package io.edugma.data.schedule.repository

import io.edugma.data.schedule.api.ScheduleService
import io.edugma.features.schedule.domain.repository.ScheduleInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ScheduleInfoRepositoryImpl(
    private val scheduleService: ScheduleService,
) : ScheduleInfoRepository {
    override fun getTeacherInfo(id: String) =
        flow { emit(scheduleService.getTeacherInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getGroupInfo(id: String) =
        flow { emit(scheduleService.getGroupInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getPlaceInfo(id: String) =
        flow { emit(scheduleService.getPlaceInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getLessonSubjectInfo(id: String) =
        flow { emit(scheduleService.getSubjectInfo(id)) }
            .flowOn(Dispatchers.IO)

    override fun getLessonTypeInfo(id: String) =
        flow { emit(scheduleService.getLessonTypeInfo(id)) }
            .flowOn(Dispatchers.IO)
}
