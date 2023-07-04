package io.edugma.data.base.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.PreferenceRepository
import io.edugma.core.api.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.serializer
import kotlin.reflect.KType
import kotlin.time.Duration

class CacheRepositoryImpl(
    private val preferenceRepository: PreferenceRepository,
) : CacheRepository {

    init {
        preferenceRepository.init(PATH)
    }

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> getInternal(key: String, type: KType, expiresIn: Duration): T? {
        if (expiresIn.isFinite()) {
            val timestamp = preferenceRepository.getLong("$VERSION_PREFIX$key") ?: return null

            val now = Clock.System.now()
            val cacheInstant = Instant.fromEpochMilliseconds(timestamp)

            if (cacheInstant.plus(expiresIn) > now) {
                return null
            }
        }

        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArray = preferenceRepository.getByteArray(key) ?: return null
        return Cbor.decodeFromByteArray(serializer, byteArray)
    }

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> getFlowInternal(
        key: String,
        type: KType,
        expiresIn: Duration,
    ): Flow<T?> {
        // TODO Посмотреть и переделать устаревание
        if (expiresIn.isFinite()) {
            val timestamp = preferenceRepository.getLong("$VERSION_PREFIX$key") ?: return flowOf(null)

            val now = Clock.System.now()
            val cacheInstant = Instant.fromEpochMilliseconds(timestamp)

            if (cacheInstant.plus(expiresIn) > now) {
                return flowOf(null)
            }
        }

        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArrayFlow = preferenceRepository.getByteArrayFlow(key)
        return byteArrayFlow.map { byteArray ->
            if (byteArray == null) {
                null
            } else {
                Cbor.decodeFromByteArray(serializer, byteArray)
            }
        }
    }

    @InternalApi
    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun <T : Any> saveInternal(key: String, value: T, type: KType) {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArray = Cbor.encodeToByteArray(serializer, value)
        preferenceRepository.saveByteArray(key, byteArray)

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
