package io.edugma.data.base.local

import android.util.Log
import io.edugma.data.base.model.PreferenceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.kodein.db.DB
import org.kodein.db.flowOf
import org.kodein.db.getById
import org.kodein.db.keyById

class PreferencesLocalDS(
    private val db: DB
) : PreferencesDS {

    override suspend fun setJson(str: String, key: String): Unit =
        withContext(Dispatchers.IO) {
            db.put(PreferenceDao(key, str))
        }

    override suspend fun getJson(key: String): Result<PreferenceDao?> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching { db.getById(key) }
        }

    override fun flowOfPreferences(key: String): Flow<PreferenceDao?> {
        return db.flowOf(db.keyById<PreferenceDao>(key))
    }

}

suspend inline fun<reified T> PreferencesDS.getJsonLazy(name: String = T::class.simpleName.orEmpty()): T? {
    return try {
        Json.decodeFromString(getJson(name).getOrNull()?.value.orEmpty())
    } catch (e: Throwable) {
        Log.e("serialize error!", "$name")
        null
    }
}


suspend inline fun<reified T> PreferencesDS.setJsonLazy(data: T, name: String = T::class.simpleName.orEmpty()) {
    setJson(Json.encodeToString(data), name)
}