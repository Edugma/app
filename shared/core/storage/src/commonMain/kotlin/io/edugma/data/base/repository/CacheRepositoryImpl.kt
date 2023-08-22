package io.edugma.data.base.repository

import io.edugma.core.api.model.CachedResult
import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.PreferenceRepository
import io.edugma.core.api.utils.InternalApi
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
        val timestamp = preferenceRepository.getLong("$VERSION_PREFIX$key") ?: return null
        val cacheInstant = Instant.fromEpochMilliseconds(timestamp)

        val data = preferenceRepository.getObjectInternal<T>(key, type) ?: return null
        return CachedResult(data, cacheInstant)
    }

    @InternalApi
    override suspend fun <T : Any> getFlowInternal(
        key: String,
        type: KType,
    ): Flow<CachedResult<T>?> {
        // TODO Посмотреть и переделать устаревание
        val timestamp = preferenceRepository.getLong("$VERSION_PREFIX$key")
            ?: return flowOf(null)
        val cacheInstant = Instant.fromEpochMilliseconds(timestamp)

        val objectFlow = preferenceRepository.getObjectFlowInternal<T>(key, type)
        return objectFlow.map { obj ->
            obj?.let { CachedResult(obj, cacheInstant) }
        }
    }

    @InternalApi
    override suspend fun <T : Any> saveInternal(key: String, value: T, type: KType) {
        preferenceRepository.saveObjectInternal(key, value, type)

        val now = Clock.System.now().epochSeconds
        preferenceRepository.saveLong("$VERSION_PREFIX$key", now)
    }

    override suspend fun remove(key: String) {
        preferenceRepository.removeByteArray(key)
    }

    override suspend fun getTimestamp(key: String): Instant? {
        val epochSeconds = preferenceRepository.getLong(VERSION_PREFIX + key) ?: return null
        return Instant.fromEpochSeconds(epochSeconds)
    }

    companion object {
        private const val PATH = "ds_cache"
        private const val VERSION_PREFIX = "__v_"
    }
}
