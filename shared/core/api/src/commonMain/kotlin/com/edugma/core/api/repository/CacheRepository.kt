package com.edugma.core.api.repository

import com.edugma.core.api.model.CachedResult
import com.edugma.core.api.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface CacheRepository {
    @InternalApi
    suspend fun <T : Any> getInternal(key: String, type: KType): CachedResult<T>?

    @InternalApi
    suspend fun <T : Any> getFlowInternal(
        key: String,
        type: KType,
    ): Flow<CachedResult<T>?>

    @InternalApi
    suspend fun <T : Any> saveInternal(key: String, value: T, type: KType, updateTimestamp: Boolean)
    suspend fun remove(key: String)
    suspend fun removeWithPrefix(prefix: String)
    suspend fun getTimestamp(key: String): Instant?
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.get(key: String): CachedResult<T>? {
    return this.getInternal<T>(key, typeOf<T>())
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.getOnlyData(key: String): T? {
    return this.getInternal<T>(key, typeOf<T>())?.data
}

// TODO придумать: чтобы не пересекались getData и get
@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.getFlow(
    key: String,
): Flow<CachedResult<T>?> {
    return this.getFlowInternal<T>(key, typeOf<T>())
}

// @OptIn(InternalApi::class)
// suspend inline fun <reified T : Any> CacheRepository.getDataFlow(
//    key: String,
// ): Flow<T?> {
//    return this.getFlowInternal<T>(key, typeOf<T>()).map { it?.data }
// }

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> CacheRepository.save(
    key: String,
    value: T,
    updateTimestamp: Boolean = true,
) {
    this.saveInternal<T>(key, value, typeOf<T>(), updateTimestamp = updateTimestamp)
}
