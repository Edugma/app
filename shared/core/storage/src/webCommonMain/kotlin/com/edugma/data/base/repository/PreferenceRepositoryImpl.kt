package com.edugma.data.base.repository

import com.edugma.core.api.repository.PreferenceRepository
import com.edugma.core.api.utils.InternalApi
import io.ktor.util.decodeBase64Bytes
import io.ktor.util.encodeBase64
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.serializer
import org.w3c.dom.Storage
import org.w3c.dom.set
import kotlin.properties.Delegates.notNull
import kotlin.reflect.KType
import kotlinx.browser.localStorage as BrowserLocalStorage

class PreferenceRepositoryImpl : PreferenceRepository {
    private val dataStore: Storage = BrowserLocalStorage
    private var prefix: String by notNull()

    private val snapshot = MutableSharedFlow<Unit>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun init(path: String) {
        prefix = path
        snapshot.tryEmit(Unit)
    }

    override suspend fun getString(key: String): String? {
        return dataStore.getItem(key)
    }
    override suspend fun getByteArray(key: String): ByteArray? {
        return dataStore.getItem(key)?.decodeBase64Bytes()
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
        return dataStore.getItem(key)?.toBooleanStrictOrNull()
    }

    override suspend fun getInt(key: String): Int? {
        return dataStore.getItem(key)?.toIntOrNull()
    }

    override suspend fun getLong(key: String): Long? {
        return dataStore.getItem(key)?.toLongOrNull()
    }

    override fun getStringFlow(key: String): Flow<String?> {
        return snapshot.map { getString(key) }.distinctUntilChanged()
    }
    override fun getByteArrayFlow(key: String): Flow<ByteArray?> {
        return snapshot.map { getByteArray(key) }.distinctUntilChanged()
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
        return snapshot.map { getBoolean(key) }.distinctUntilChanged()
    }

    override fun getIntFlow(key: String): Flow<Int?> {
        return snapshot.map { getInt(key) }.distinctUntilChanged()
    }

    override fun getLongFlow(key: String): Flow<Long?> {
        return snapshot.map { getLong(key) }.distinctUntilChanged()
    }

    override suspend fun saveString(key: String, value: String) {
        snapshot.tryEmit(Unit)
        dataStore.set(key, value)
    }
    override suspend fun saveBoolean(key: String, value: Boolean) {
        snapshot.tryEmit(Unit)
        dataStore.set(key, value.toString())
    }

    override suspend fun saveInt(key: String, value: Int) {
        snapshot.tryEmit(Unit)
        dataStore.set(key, value.toString())
    }

    override suspend fun saveLong(key: String, value: Long) {
        snapshot.tryEmit(Unit)
        dataStore.set(key, value.toString())
    }
    override suspend fun saveByteArray(key: String, value: ByteArray) {
        snapshot.tryEmit(Unit)
        dataStore.set(key, value.encodeBase64())
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
        snapshot.tryEmit(Unit)
        dataStore.removeItem(key)
    }
    override suspend fun removeBoolean(key: String) {
        snapshot.tryEmit(Unit)
        dataStore.removeItem(key)
    }

    override suspend fun removeInt(key: String) {
        snapshot.tryEmit(Unit)
        dataStore.removeItem(key)
    }

    override suspend fun removeLong(key: String) {
        snapshot.tryEmit(Unit)
        dataStore.removeItem(key)
    }
    override suspend fun removeByteArray(key: String) {
        snapshot.tryEmit(Unit)
        dataStore.removeItem(key)
    }

    override suspend fun removeByteArrayWithPrefix(prefix: String) {
        snapshot.tryEmit(Unit)
        (0..<dataStore.length)
        repeat(dataStore.length) { keyIndex ->
            val key = dataStore.key(keyIndex)
            if (key != null && key.startsWith(prefix)) {
                dataStore.removeItem(key)
            }
        }
    }

    override suspend fun removeObject(key: String) {
        snapshot.tryEmit(Unit)
        dataStore.removeItem(key)
    }
}
