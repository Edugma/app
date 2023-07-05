package io.edugma.core.api.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    fun init(path: String)

    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?
    suspend fun getLong(key: String): Long?
    suspend fun getByteArray(key: String): ByteArray?

    fun getStringFlow(key: String): Flow<String?>
    fun getBooleanFlow(key: String): Flow<Boolean?>
    fun getLongFlow(key: String): Flow<Long?>
    fun getByteArrayFlow(key: String): Flow<ByteArray?>

    suspend fun saveString(key: String, value: String)
    suspend fun saveBoolean(key: String, value: Boolean)
    suspend fun saveLong(key: String, value: Long)
    suspend fun saveByteArray(key: String, value: ByteArray)

    suspend fun removeString(key: String)
    suspend fun removeBoolean(key: String)
    suspend fun removeLong(key: String)
    suspend fun removeByteArray(key: String)
}
