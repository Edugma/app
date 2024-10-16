package com.edugma.core.api.repository

import com.edugma.core.api.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KType
import kotlin.reflect.typeOf

interface SettingsRepository {
    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?
    suspend fun getInt(key: String): Int?

    @InternalApi
    suspend fun <T : Any> getInternal(key: String, type: KType): T?

    fun getStringFlow(key: String): Flow<String?>
    fun getBooleanFlow(key: String): Flow<Boolean?>
    fun getIntFlow(key: String): Flow<Int?>

    @InternalApi
    fun <T : Any> getFlowInternal(key: String, type: KType): Flow<T?>

    suspend fun saveString(key: String, value: String)
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun saveInt(key: String, value: Int)

    suspend fun removeString(key: String)
    suspend fun removeBoolean(key: String)
    suspend fun removeInt(key: String)

    @InternalApi
    suspend fun <T : Any> saveInternal(key: String, value: T, type: KType)
    suspend fun removeObject(key: String)
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> SettingsRepository.get(key: String): T? {
    return this.getInternal<T>(key, typeOf<T>())
}

@OptIn(InternalApi::class)
inline fun <reified T : Any> SettingsRepository.getFlow(key: String): Flow<T?> {
    return this.getFlowInternal<T>(key, typeOf<T>())
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> SettingsRepository.save(key: String, value: T) {
    this.saveInternal<T>(key, value, typeOf<T>())
}

@OptIn(InternalApi::class)
suspend inline fun <reified T : Any> SettingsRepository.saveOrRemove(key: String, value: T?) {
    if (value == null) {
        this.removeObject(key)
    } else {
        this.saveInternal<T>(key, value, typeOf<T>())
    }
}
