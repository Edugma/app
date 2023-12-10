package io.edugma.data.schedule.api

import io.edugma.core.api.api.EdugmaHttpClient
import io.edugma.core.api.api.get
import io.edugma.core.api.api.getResult
import io.edugma.core.api.api.postResult
import io.edugma.core.api.model.PagingDto
import io.edugma.features.schedule.domain.model.ScheduleComplexFilter
import io.edugma.features.schedule.domain.model.compact.CompactPlaceInfo
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import io.edugma.features.schedule.domain.model.group.GroupInfo
import io.edugma.features.schedule.domain.model.place.PlaceDailyOccupancy
import io.edugma.features.schedule.domain.model.place.PlaceFilters
import io.edugma.features.schedule.domain.model.source.ScheduleSourceFull
import io.edugma.features.schedule.domain.model.source.ScheduleSourceType
import io.edugma.features.schedule.domain.model.teacher.TeacherInfo

class ScheduleService(
    private val client: EdugmaHttpClient,
) {
    // Compact
    suspend fun getCompactSchedule(type: String, key: String): Result<CompactSchedule> =
        client.getResult("$PREFIX-compact-schedule") {
            param("type", type)
            param("key", key)
        }

    suspend fun getComplexSchedule(filter: ScheduleComplexFilter): Result<CompactSchedule> =
        client.postResult("$PREFIX-compact-complex") {
            body(filter)
        }

    suspend fun getSources(
        type: String,
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<ScheduleSourceFull> =
        client.get("$PREFIX-sources") {
            param("type", type)
            param("query", query)
            param("page", page)
            param("limit", limit)
        }

    suspend fun getSourceTypes(): List<ScheduleSourceType> =
        client.get("$PREFIX-sources-types")

    suspend fun getGroupInfo(id: String): GroupInfo =
        client.get("$PREFIX-info-group") {
            param("id", id)
        }

    suspend fun getTeacherInfo(id: String): TeacherInfo =
        client.get("$PREFIX-info-teacher") {
            param("id", id)
        }

    suspend fun getPlaceInfo(id: String): CompactPlaceInfo =
        client.get("$PREFIX-info-place") {
            param("id", id)
        }

    suspend fun findFreePlaces(filters: PlaceFilters): Result<Map<CompactPlaceInfo, Int>> =
        client.postResult("$PREFIX-places-free") {
            body(filters)
        }

    suspend fun getPlaceOccupancy(placeId: String): Result<List<PlaceDailyOccupancy>> =
        client.getResult("$PREFIX-places-occupancy") {
            param("placeId", placeId)
        }

    companion object {
        private const val PREFIX = "schedule"
    }
}
