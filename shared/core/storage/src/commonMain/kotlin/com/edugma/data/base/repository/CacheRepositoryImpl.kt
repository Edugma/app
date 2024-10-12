package com.edugma.data.base.repository

import co.touchlab.kermit.Logger
import com.edugma.core.api.model.CachedResult
import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.PreferenceRepository
import com.edugma.core.api.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.reflect.KType

class CacheRepositoryImpl(
    private val preferenceRepository: PreferenceRepository,
) : CacheRepository {

    init {
        preferenceRepository.init(PATH)
    }

    @InternalApi
    override suspend fun <T : Any> getInternal(key: String, type: KType): CachedResult<T>? {
        val cacheInstant = getTimestamp(key) ?: return null

        val data = preferenceRepository.getObjectInternal<T>(key, type) ?: return null
        return CachedResult(data, cacheInstant)
    }

    @InternalApi
    override suspend fun <T : Any> getFlowInternal(
        key: String,
        type: KType,
    ): Flow<CachedResult<T>?> {
        // TODO Посмотреть и переделать устаревание
        val cacheInstant = getTimestamp(key) ?: return flowOf(null)

        val objectFlow = preferenceRepository.getObjectFlowInternal<T>(key, type)
        return objectFlow.map { obj ->
            obj?.let { CachedResult(obj, cacheInstant) }
        }
    }

    @InternalApi
    override suspend fun <T : Any> saveInternal(
        key: String,
        value: T,
        type: KType,
        updateTimestamp: Boolean
    ) {
        Logger.d("Save cache by key=$key", tag = TAG)
        preferenceRepository.saveObjectInternal(key, value, type)

        if (updateTimestamp) {
            saveTimestamp(key)
        } else {
            saveTimestamp(key, Instant.DISTANT_PAST)
        }
    }

    override suspend fun remove(key: String) {
        preferenceRepository.removeByteArray(key)
    }

    override suspend fun removeWithPrefix(prefix: String) {
        preferenceRepository.removeByteArrayWithPrefix(prefix)
    }

    override suspend fun getTimestamp(key: String): Instant? {
        val epochSeconds = preferenceRepository.getLong(VERSION_PREFIX + key) ?: return null
        return Instant.fromEpochSeconds(epochSeconds)
    }

    private suspend fun saveTimestamp(key: String, timestamp: Instant = Clock.System.now()) {
        preferenceRepository.saveLong("$VERSION_PREFIX$key", timestamp.epochSeconds)
    }

    companion object {
        private const val PATH = "ds_cache"
        private const val VERSION_PREFIX = "__v_"
        private const val TAG = "CacheRepository"
    }
}
