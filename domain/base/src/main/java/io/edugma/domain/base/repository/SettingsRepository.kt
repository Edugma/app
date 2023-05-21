package io.edugma.domain.base.repository

interface SettingsRepository {
    suspend fun getString(key: String): String?
    suspend fun getBoolean(key: String): Boolean?

    suspend fun saveString(key: String, value: String)
    suspend fun saveBoolean(key: String, value: Boolean)
}
