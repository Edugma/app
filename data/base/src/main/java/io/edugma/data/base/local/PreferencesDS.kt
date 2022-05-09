package io.edugma.data.base.local

import android.util.Log
import io.edugma.data.base.model.PreferenceDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

interface PreferencesDS {
    suspend fun setString(str: String, key: String)
    suspend fun getPreference(key: String): Result<PreferenceDao?>
    suspend fun delete(key: String)
    fun flowOfPreferences(key: String): Flow<PreferenceDao?>
}

suspend inline fun <reified T> PreferencesDS.set(obj: T, key: String): Result<Unit> {
    return kotlin.runCatching {
        val str = Json.encodeToString(obj)
        setString(str, key)
    }
}

suspend inline fun <reified T> PreferencesDS.get(key: String): Result<T?> {
    val json = getPreference(key)
    return json.mapCatching { it?.let { Json.decodeFromString(it.value) } }
}

suspend fun PreferencesDS.getSourceValue(key: String): String? {
    return getPreference(key).mapCatching { it?.value }.getOrNull()
}

inline fun <reified T> PreferencesDS.flowOf(key: String): Flow<Result<T?>> {
    return flowOfPreferences(key)
        .map { kotlin.runCatching { it?.let { Json.decodeFromString(it.value) } } }
}

suspend inline fun <reified T> PreferencesDS.get(key: String, defaultValue: T): T {
    return get<T>(key).getOrNull() ?: defaultValue
}

suspend inline fun<reified T> PreferencesDS.getJsonLazy(name: String = T::class.simpleName.orEmpty()): T? {
    return get<T>(name).getOrNull()
}

suspend inline fun<reified T> PreferencesDS.setJsonLazy(data: T, name: String = T::class.simpleName.orEmpty()) {
    set(data, name)
}

suspend inline fun<reified T> PreferencesDS.deleteJsonLazy(name: String = T::class.simpleName.orEmpty()) {
    delete(name)
}