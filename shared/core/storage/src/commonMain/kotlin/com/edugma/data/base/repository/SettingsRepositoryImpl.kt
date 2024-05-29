package com.edugma.data.base.repository

import com.edugma.core.api.repository.PreferenceRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.utils.InternalApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.serializer
import kotlin.reflect.KType

class SettingsRepositoryImpl(
    private val preferenceRepository: PreferenceRepository,
) : SettingsRepository {

    init {
        preferenceRepository.init(PATH)
    }

    override suspend fun getString(key: String): String? {
        return preferenceRepository.getString(key)
    }

    override suspend fun getBoolean(key: String): Boolean? {
        return preferenceRepository.getBoolean(key)
    }

    override suspend fun getInt(key: String): Int? {
        return preferenceRepository.getInt(key)
    }

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> getInternal(key: String, type: KType): T? {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArray = preferenceRepository.getByteArray(key) ?: return null
        return Cbor.decodeFromByteArray(serializer, byteArray)
    }

    override fun getStringFlow(key: String): Flow<String?> {
        return preferenceRepository.getStringFlow(key)
    }
    override fun getBooleanFlow(key: String): Flow<Boolean?> {
        return preferenceRepository.getBooleanFlow(key)
    }

    override fun getIntFlow(key: String): Flow<Int?> {
        return preferenceRepository.getIntFlow(key)
    }

    @InternalApi
    @OptIn(ExperimentalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getFlowInternal(key: String, type: KType): Flow<T?> {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArrayFlow = preferenceRepository.getByteArrayFlow(key)
        return byteArrayFlow.map { byteArray ->
            byteArray?.let {
                Cbor.decodeFromByteArray(serializer, byteArray)
            }
        }
    }

    override suspend fun saveString(key: String, value: String) {
        preferenceRepository.saveString(key, value)
    }
    override suspend fun saveBoolean(key: String, value: Boolean) {
        preferenceRepository.saveBoolean(key, value)
    }
    override suspend fun saveInt(key: String, value: Int) {
        preferenceRepository.saveInt(key, value)
    }

    override suspend fun removeBoolean(key: String) {
        preferenceRepository.removeBoolean(key)
    }
    override suspend fun removeString(key: String) {
        preferenceRepository.removeString(key)
    }
    override suspend fun removeInt(key: String) {
        preferenceRepository.removeInt(key)
    }

    @InternalApi
    @Suppress("UNCHECKED_CAST")
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun <T : Any> saveInternal(key: String, value: T, type: KType) {
        val serializer = Cbor.serializersModule.serializer(type) as KSerializer<T>
        val byteArray = Cbor.encodeToByteArray(serializer, value)
        preferenceRepository.saveByteArray(key, byteArray)
    }

    override suspend fun <T : Any> remove(key: String) {
        preferenceRepository.removeByteArray(key)
    }

    companion object {
        private const val PATH = "ds_settings"
    }
}
