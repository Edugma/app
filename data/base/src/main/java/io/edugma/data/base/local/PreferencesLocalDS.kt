package io.edugma.data.base.local

import io.edugma.data.base.model.PreferenceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
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