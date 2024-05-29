package com.edugma.core.api.repository

import com.edugma.core.api.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface PreferenceRepository {
    fun init(path: String)

    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?
    suspend fun getInt(key: String): Int?
    suspend fun getLong(key: String): Long?
    suspend fun getByteArray(key: String): ByteArray?

    @InternalApi
    suspend fun <T> getObjectInternal(key: String, type: KType): T?

    fun getStringFlow(key: String): Flow<String?>
    fun getBooleanFlow(key: String): Flow<Boolean?>
    fun getIntFlow(key: String): Flow<Int?>
    fun getLongFlow(key: String): Flow<Long?>
    fun getByteArrayFlow(key: String): Flow<ByteArray?>

    @InternalApi
    fun <T> getObjectFlowInternal(key: String, type: KType): Flow<T?>

    suspend fun saveString(key: String, value: String)
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun saveInt(key: String, value: Int)
    suspend fun saveLong(key: String, value: Long)
    suspend fun saveByteArray(key: String, value: ByteArray)

    @InternalApi
    suspend fun <T> saveObjectInternal(key: String, value: T, type: KType)

    suspend fun removeString(key: String)
    suspend fun removeBoolean(key: String)
    suspend fun removeInt(key: String)
    suspend fun removeLong(key: String)
    suspend fun removeByteArray(key: String)
    suspend fun removeObject(key: String)
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> PreferenceRepository.getObject(key: String): T? {
    return this.getObjectInternal<T>(key, typeOf<T>())
}

@OptIn(InternalApi::class)
inline fun <reified T : Any> PreferenceRepository.getObjectFlow(key: String): Flow<T?> {
    return this.getObjectFlowInternal<T>(key, typeOf<T>())
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> PreferenceRepository.saveObject(key: String, value: T) {
    this.saveObjectInternal<T>(key, value, typeOf<T>())
}
