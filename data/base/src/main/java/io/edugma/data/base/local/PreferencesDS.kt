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
    suspend fun getString(key: String): Result<PreferenceDao?>
    fun flowOfPreferences(key: String): Flow<PreferenceDao?>
}

suspend inline fun <reified T> PreferencesDS.set(obj: T, key: String): Result<Unit> {
    return kotlin.runCatching {
        val str = Json.encodeToString(obj)
        setString(str, key)
    }
}

suspend inline fun <reified T> PreferencesDS.get(key: String): Result<T?> {
    val json = getString(key)
    return json.mapCatching { it?.let { Json.decodeFromString(it.value) } }
}

inline fun <reified T> PreferencesDS.flowOf(key: String): Flow<Result<T?>> {
    return flowOfPreferences(key)
        .map { kotlin.runCatching { it?.let { Json.decodeFromString(it.value) } } }
}

suspend inline fun <reified T> PreferencesDS.get(key: String, defaultValue: T): T {
    return get<T>(key).getOrNull() ?: defaultValue
}

suspend inline fun<reified T> PreferencesDS.getJsonLazy(name: String = T::class.simpleName.orEmpty()): T? {
    return try {
        Json.decodeFromString(getString(name).getOrNull()?.value.orEmpty())
    } catch (e: Throwable) {
        Log.e("serialize error!", name)
        null
    }
}

suspend inline fun<reified T> PreferencesDS.setJsonLazy(data: T, name: String = T::class.simpleName.orEmpty()) {
    setString(Json.encodeToString(data), name)
}