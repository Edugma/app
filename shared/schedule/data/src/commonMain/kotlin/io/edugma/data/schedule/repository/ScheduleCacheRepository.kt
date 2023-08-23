package io.edugma.data.schedule.repository

import io.edugma.core.api.hash.Hash
import io.edugma.core.api.model.CachedResult
import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.PreferenceRepository
import io.edugma.core.api.repository.getFlow
import io.edugma.core.api.repository.getObject
import io.edugma.core.api.repository.getObjectFlow
import io.edugma.core.api.repository.save
import io.edugma.core.api.repository.saveObject
import io.edugma.core.api.utils.runCoCatching
import io.edugma.data.base.consts.CacheConst
import io.edugma.features.schedule.domain.model.compact.CompactSchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class ScheduleCacheRepository(
    private val cacheRepository: CacheRepository,
    private val preferenceRepository: PreferenceRepository,
) {
    init {
        preferenceRepository.init(PATH)
    }

    suspend fun getSchedule(key: String): Flow<CachedResult<CompactSchedule>?> {
        return cacheRepository.getFlow<CompactSchedule>(CacheConst.Schedule + key)
    }

    suspend fun getScheduleHistoryRecord(key: String, timestamp: Instant): CachedResult<CompactSchedule>? {
        val scheduleHistoryKey = CacheConst.Schedule + key + "_history"
        return preferenceRepository.getObject<List<CachedResult<CompactSchedule>>>(
            scheduleHistoryKey,
        )?.firstOrNull { it.timestamp == timestamp }
    }

    fun getScheduleHistory(key: String): Flow<List<CachedResult<CompactSchedule>>?> {
        val scheduleHistoryKey = CacheConst.Schedule + key + "_history"
        return preferenceRepository.getObjectFlow<List<CachedResult<CompactSchedule>>>(
            scheduleHistoryKey,
        )
    }

    suspend fun saveSchedule(key: String, data: CompactSchedule) {
        val scheduleKey = CacheConst.Schedule + key
        val prevScheduleHashKey = scheduleKey + "_prev"

        val newHash = Hash.hash(data.toString())

        val previousScheduleHash = preferenceRepository.getInt(prevScheduleHashKey)?.toUInt()
        if (newHash != previousScheduleHash) {
            cacheRepository.save(scheduleKey, data)
            updateHistory(scheduleKey, data)
            preferenceRepository.saveInt(prevScheduleHashKey, newHash.toInt())
        }
    }

    private suspend fun updateHistory(
        scheduleKey: String,
        data: CompactSchedule,
    ) {
        val scheduleHistoryKey = scheduleKey + "_history"
        // If model changed
        val scheduleHistoryList2 = runCoCatching {
            preferenceRepository.getObject<List<CachedResult<CompactSchedule>>>(
                scheduleHistoryKey,
            )
        }

        val scheduleHistoryList = scheduleHistoryList2.getOrNull()

        val timestamp = Clock.System.now()

        if (scheduleHistoryList != null) {
            val resList = scheduleHistoryList.toMutableList()
            if (resList.size >= SCHEDULE_HISTORY_MAX_SIZE) {
                repeat(resList.size - SCHEDULE_HISTORY_MAX_SIZE + 1) {
                    resList.removeAt(SCHEDULE_HISTORY_MAX_SIZE - 1)
                }
            }
            resList.add(CachedResult(data, timestamp))
            preferenceRepository.saveObject<List<CachedResult<CompactSchedule>>>(
                scheduleHistoryKey,
                resList,
            )
        } else {
            preferenceRepository.saveObject<List<CachedResult<CompactSchedule>>>(
                scheduleHistoryKey,
                listOf(CachedResult(data, timestamp)),
            )
        }
    }

    companion object {
        private const val PATH = "ds_schedule_cache"
        private const val SCHEDULE_HISTORY_MAX_SIZE = 5
    }
}
