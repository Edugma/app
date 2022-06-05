package io.edugma.data.base.local

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface CacheDS<T : Any> {
    fun save(kClass: KType, obj: T, key: String): Result<Unit>
    fun get(kClass: KType, key: String): Result<T?>
    fun flowOfDao(kClass: KType, key: String): Flow<Result<T?>>
}

inline fun <reified T : Any> CacheDS<T>.save(obj: T, key: String): Result<Unit> {
    return save(typeOf<T>(), obj, key)
}

inline fun <reified T : Any> CacheDS<T>.get(key: String): Result<T?> {
    return get(typeOf<T>(), key)
}

inline fun <reified T : Any> CacheDS<T>.flowOf(key: String): Flow<Result<T?>> {
    return flowOfDao(typeOf<T>(), key)
}