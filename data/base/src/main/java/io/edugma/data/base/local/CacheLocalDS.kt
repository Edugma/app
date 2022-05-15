package io.edugma.data.base.local

import io.edugma.data.base.model.CacheDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import org.kodein.db.DB
import org.kodein.db.flowOf
import org.kodein.db.getById
import org.kodein.db.keyById

class CacheLocalDS(
    private val db: DB
) {
    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> save(obj: T, key: String): Result<Unit> {
        return kotlin.runCatching {
            val data = Cbor.encodeToByteArray(obj)
            save(data, key)
        }
    }

    fun save(value: ByteArray, key: String) {
        db.put(CacheDao(key, value))
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> get(key: String): Result<T?> {
        val serializedData = getSerializedData(key)
        return serializedData.mapCatching { it?.let { Cbor.decodeFromByteArray(it.value) } }
    }

    fun getSerializedData(key: String): Result<CacheDao?> {
        return kotlin.runCatching {
            db.getById(key)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> flowOf(key: String): Flow<Result<T?>> {
        return flowOfDao(key).map {
            kotlin.runCatching {
                it?.let { Cbor.decodeFromByteArray(it.value) }
            }
        }
    }

    fun flowOfDao(key: String): Flow<CacheDao?> {
        return db.flowOf(db.keyById(key))
    }
}