package com.edugma.data.base.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.edugma.core.api.repository.PathRepository
import com.edugma.core.api.repository.PreferenceRepository
import com.edugma.core.api.utils.IO
import com.edugma.core.api.utils.InternalApi
import com.edugma.data.base.utils.DataStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.serializer
import kotlin.reflect.KType

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

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> getObjectInternal(key: String, type: KType): T? {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArray = getByteArray(key) ?: return null
        return Cbor.decodeFromByteArray(serializer, byteArray)
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return dataStore.data.first()[booleanPreferencesKey(key)]
    }

    override suspend fun getInt(key: String): Int? {
        return dataStore.data.first()[intPreferencesKey(key)]
    }

    override suspend fun getLong(key: String): Long? {
        return dataStore.data.first()[longPreferencesKey(key)]
    }

    override fun getStringFlow(key: String): Flow<String?> {
        return dataStore.data.map { it[stringPreferencesKey(key)] }.distinctUntilChanged()
    }
    override fun getByteArrayFlow(key: String): Flow<ByteArray?> {
        return dataStore.data.map { it[byteArrayPreferencesKey(key)] }.distinctUntilChanged()
    }

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun <T> getObjectFlowInternal(key: String, type: KType): Flow<T?> {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArrayFlow = getByteArrayFlow(key)
        return byteArrayFlow.map { byteArray ->
            byteArray?.let { Cbor.decodeFromByteArray(serializer, byteArray) }
        }.distinctUntilChanged()
    }

    override fun getBooleanFlow(key: String): Flow<Boolean?> {
        return dataStore.data.map { it[booleanPreferencesKey(key)] }.distinctUntilChanged()
    }

    override fun getIntFlow(key: String): Flow<Int?> {
        return dataStore.data.map { it[intPreferencesKey(key)] }.distinctUntilChanged()
    }

    override fun getLongFlow(key: String): Flow<Long?> {
        return dataStore.data.map { it[longPreferencesKey(key)] }.distinctUntilChanged()
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

    override suspend fun saveInt(key: String, value: Int) {
        dataStore.edit { settings ->
            settings[intPreferencesKey(key)] = value
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

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> saveObjectInternal(key: String, value: T, type: KType) {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArray = Cbor.encodeToByteArray(serializer, value)
        saveByteArray(key, byteArray)
    }

    override suspend fun removeString(key: String) {
        dataStore.edit { settings ->
            settings.remove(stringPreferencesKey(key))
        }
    }
    override suspend fun removeBoolean(key: String) {
        dataStore.edit { settings ->
            settings.remove(booleanPreferencesKey(key))
        }
    }

    override suspend fun removeInt(key: String) {
        dataStore.edit { settings ->
            settings.remove(intPreferencesKey(key))
        }
    }

    override suspend fun removeLong(key: String) {
        dataStore.edit { settings ->
            settings.remove(longPreferencesKey(key))
        }
    }
    override suspend fun removeByteArray(key: String) {
        dataStore.edit { settings ->
            settings.remove(byteArrayPreferencesKey(key))
        }
    }

    override suspend fun removeObject(key: String) {
        dataStore.edit { settings ->
            settings.remove(byteArrayPreferencesKey(key))
        }
    }
}
