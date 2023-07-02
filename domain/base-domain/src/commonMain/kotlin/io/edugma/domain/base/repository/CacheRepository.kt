package io.edugma.domain.base.repository

import io.edugma.domain.base.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.reflect.KType
import kotlin.reflect.typeOf
import kotlin.time.Duration

interface CacheRepository {
    @InternalApi
    suspend fun <T : Any> getInternal(key: String, type: KType, expiresIn: Duration): T?

    @InternalApi
    suspend fun <T : Any> getFlowInternal(key: String, type: KType, expiresIn: Duration): Flow<T?>

    @InternalApi
    suspend fun <T : Any> saveInternal(key: String, value: T, type: KType)
    suspend fun remove(key: String)
    suspend fun getTimestamp(key: String): Instant?
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.get(key: String, expiresIn: Duration = Duration.INFINITE): T? {
    return this.getInternal<T>(key, typeOf<T>(), expiresIn)
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.getFlow(key: String, expiresIn: Duration = Duration.INFINITE): Flow<T?> {
    return this.getFlowInternal<T>(key, typeOf<T>(), expiresIn)
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.save(key: String, value: T) {
    this.saveInternal<T>(key, value, typeOf<T>())
}
