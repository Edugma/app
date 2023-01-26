package io.edugma.data.base.local

import io.edugma.data.base.model.DataVersion
import io.edugma.data.base.model.isExpired
import kotlinx.datetime.Instant
import org.kodein.db.DB
import org.kodein.db.get
import org.kodein.db.keyById
import kotlin.time.Duration

class DataVersionLocalDS(
    private val db: DB,
) {
//    inline fun <reified T> getVersion(vararg keys: String): DataVersion? {
//        return kotlin.runCatching {
//            val classQualifier = T::class.qualifiedName
//            val fullKey = classQualifier + keys.joinToString(separator = "")
//            getVersion(fullKey)
//        }.getOrNull()
//    }

    fun getVersion(vararg keys: String): DataVersion? {
        return kotlin.runCatching {
            val fullKey = keys.joinToString(separator = "")
            getVersion(fullKey)
        }.getOrNull()
    }

    fun getVersion(id: String): DataVersion? {
        return kotlin.runCatching {
            val key = db.keyById<DataVersion>(id)
            db.get<DataVersion>(key)
        }.getOrNull()
    }
//
//    inline fun <reified T> setVersion(dateTime: ZonedDateTime, vararg keys: String) {
//        kotlin.runCatching {
//            val classQualifier = T::class.qualifiedName
//            val fullKey = classQualifier + keys.joinToString(separator = "")
//            setVersion(fullKey, dateTime)
//        }
//    }

    fun setVersion(dateTime: Instant, vararg keys: String) {
        kotlin.runCatching {
            val fullKey = keys.joinToString(separator = "")
            setVersion(fullKey, dateTime)
        }
    }

    fun setVersion(id: String, dateTime: Instant) {
        kotlin.runCatching {
            db.put(DataVersion(id, dateTime))
        }
    }
}

fun DataVersionLocalDS.isExpired(duration: Duration, vararg keys: String): Boolean {
    return this.getVersion(*keys)?.isExpired(duration) ?: true
}
