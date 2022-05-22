package io.edugma.data.base.local

import android.util.Log
import io.edugma.data.base.model.Cached
import io.edugma.domain.base.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import java.time.ZonedDateTime
import kotlin.time.Duration

inline fun <reified T> DataVersionLocalDS.getFlow(
    key: String,
    expire: Duration,
    prefix: String = T::class.qualifiedName ?: "",
    crossinline onGet: (key: String) -> Result<T>
): Flow<Cached<T?>> {
    return flow { emit(get(key, expire, prefix, onGet)) }
}

inline fun <reified T> DataVersionLocalDS.get(
    key: String,
    expire: Duration,
    prefix: String = T::class.qualifiedName ?: "",
    onGet: (key: String) -> Result<T>
): Cached<T?> {
    val obj = onGet(key)
    val isExpired = isExpired(expire, prefix + key)
    return Cached(obj, isExpired)
}

inline fun <reified T> DataVersionLocalDS.save(
    obj: T,
    key: String,
    prefix: String = T::class.qualifiedName ?: "",
    onSave: (obj: T, key: String) -> Result<Unit>
): Result<Unit> {
    return onSave(obj, key).onSuccess {
        setVersion(Clock.System.now(), prefix + key)
    }.onFailure {
        Log.e(TAG, it.toString(), it)
    }
}