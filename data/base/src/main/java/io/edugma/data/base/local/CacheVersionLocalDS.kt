package io.edugma.data.base.local

import io.edugma.data.base.model.Cached
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.ZonedDateTime
import kotlin.time.Duration

class CacheVersionLocalDS(
    val cache: CacheLocalDS,
    val version: DataVersionLocalDS
) {
    val prefix = "__cache__"

    inline fun <reified T> save(obj: T, key: String): Result<Unit> {
        return cache.save(obj, key).onSuccess {
            version.setVersion(ZonedDateTime.now(), prefix + key)
        }
    }

    inline fun <reified T> get(key: String, expire: Duration): Cached<T?> {
        val obj = cache.get<T>(key)
        val isExpired = version.isExpired(expire, prefix + key)
        return Cached(obj, isExpired)
    }

    inline fun <reified T> getFlow(key: String, expire: Duration): Flow<Cached<T?>> {
        return flow { emit(get(key, expire)) }
    }
}