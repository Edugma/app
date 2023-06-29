package io.edugma.data.base.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.edugma.data.base.utils.DataStoreFactory
import io.edugma.domain.base.repository.PathRepository
import io.edugma.domain.base.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl(
    private val pathRepository: PathRepository,
) : PreferenceRepository {
    private lateinit var dataStore: DataStore<Preferences>
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun init(path: String) {
        dataStore = DataStoreFactory.createDataStore(
            coroutineScope = scope,
            producePath = { pathRepository.getDatastorePath("$path.preferences_pb") },
        )
    }

    override suspend fun getString(key: String): String? {
        return dataStore.data.first()[stringPreferencesKey(key)]
    }
    override suspend fun getByteArray(key: String): ByteArray? {
        return dataStore.data.first()[byteArrayPreferencesKey(key)]
    }
    override suspend fun getBoolean(key: String): Boolean? {
        return dataStore.data.first()[booleanPreferencesKey(key)]
    }
    override suspend fun getLong(key: String): Long? {
        return dataStore.data.first()[longPreferencesKey(key)]
    }

    override fun getStringFlow(key: String): Flow<String?> {
        return dataStore.data.map { it[stringPreferencesKey(key)] }
    }
    override fun getByteArrayFlow(key: String): Flow<ByteArray?> {
        return dataStore.data.map { it[byteArrayPreferencesKey(key)] }
    }
    override fun getBooleanFlow(key: String): Flow<Boolean?> {
        return dataStore.data.map { it[booleanPreferencesKey(key)] }
    }
    override fun getLongFlow(key: String): Flow<Long?> {
        return dataStore.data.map { it[longPreferencesKey(key)] }
    }

    override suspend fun saveString(key: String, value: String) {
        dataStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
        }
    }
    override suspend fun saveBoolean(key: String, value: Boolean) {
        dataStore.edit { settings ->
            settings[booleanPreferencesKey(key)] = value
        }
    }
    override suspend fun saveLong(key: String, value: Long) {
        dataStore.edit { settings ->
            settings[longPreferencesKey(key)] = value
        }
    }
    override suspend fun saveByteArray(key: String, value: ByteArray) {
        dataStore.edit { settings ->
            settings[byteArrayPreferencesKey(key)] = value
        }
    }

    override suspend fun removeString(key: String) {
        dataStore.edit { settings ->
            settings.remove(stringPreferencesKey(key))
        }
    }
    override suspend fun removeBoolean(key: String) {
        dataStore.edit { settings ->
            settings.remove(longPreferencesKey(key))
        }
    }
    override suspend fun removeLong(key: String) {
        dataStore.edit { settings ->
            settings.remove(booleanPreferencesKey(key))
        }
    }
    override suspend fun removeByteArray(key: String) {
        dataStore.edit { settings ->
            settings.remove(byteArrayPreferencesKey(key))
        }
    }
}
