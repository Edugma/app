package io.edugma.data.base.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.edugma.data.base.local.DataStoreFactory
import io.edugma.domain.base.repository.PathRepository
import io.edugma.domain.base.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class SettingsRepositoryImpl(
    pathRepository: PathRepository,
) : SettingsRepository {
    private val dataStore: DataStore<Preferences>
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        dataStore = DataStoreFactory.createDataStore(
            coroutineScope = scope,
            producePath = { pathRepository.getDatastorePath(SETTINGS_PATH) },
        )
    }

    override suspend fun getString(key: String): String? {
        return dataStore.data.first()[stringPreferencesKey(key)]
    }

    override suspend fun saveString(key: String, value: String) {
        dataStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return dataStore.data.first()[booleanPreferencesKey(key)]
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore.edit { settings ->
            settings[booleanPreferencesKey(key)] = value
        }
    }

    companion object {
        private const val SETTINGS_PATH = "ds_settings.preferences_pb"
    }
}
