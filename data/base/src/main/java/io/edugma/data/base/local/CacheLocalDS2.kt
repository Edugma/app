package io.edugma.data.base.local

import io.edugma.data.base.model.CacheDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.*
import kotlinx.serialization.cbor.Cbor
import org.kodein.db.DB
import org.kodein.db.flowOf
import org.kodein.db.getById
import org.kodein.db.keyById
import kotlin.reflect.KClass
import kotlin.reflect.KType

class CacheLocalDS2(
    private val db: DB
) : CacheDS<CacheDao> {


    override fun save(kClass: KType, obj: CacheDao, key: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun get(kClass: KType, key: String): Result<CacheDao?> {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun flowOfDao(kClass: KType, key: String): Flow<Result<CacheDao?>> {
        return flowOfDao(key).map {
            kotlin.runCatching {
                it?.let {
                    val serializer = Cbor.serializersModule.serializer(kClass) as KSerializer<CacheDao>
                    Cbor.decodeFromByteArray(serializer, it.value)
                }
            }
        }
    }

    fun flowOfDao(key: String): Flow<CacheDao?> {
        return db.flowOf(db.keyById(key))
    }


    //    fun save(value: ByteArray, key: String) {
//        db.put(CacheDao(key, value))
//    }
//
//    fun getSerializedData(key: String): Result<CacheDao?> {
//        return kotlin.runCatching {
//            db.getById(key)
//        }
//    }
//
//    fun flowOfDao(key: String): Flow<CacheDao?> {
//        return db.flowOf(db.keyById(key))
//    }
//
//
//    @OptIn(ExperimentalSerializationApi::class)
//    inline fun <reified T> save(obj: T, key: String): Result<Unit> {
//        return kotlin.runCatching {
//            val data = Cbor.encodeToByteArray(obj)
//            save(data, key)
//        }
//    }
//
//
//
//    @OptIn(ExperimentalSerializationApi::class)
//    inline fun <reified T> get(key: String): Result<T?> {
//        val serializedData = getSerializedData(key)
//        return serializedData.mapCatching { it?.let { Cbor.decodeFromByteArray(it.value) } }
//    }
//
//
//
//    @OptIn(ExperimentalSerializationApi::class)
//    inline fun <reified T> flowOf(key: String): Flow<Result<T?>> {
//        return flowOfDao(key).map {
//            kotlin.runCatching {
//                it?.let { Cbor.decodeFromByteArray(it.value) }
//            }
//        }
//    }
}