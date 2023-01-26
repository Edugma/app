package io.edugma.features.base.core.navigation.core

import android.util.Log
import io.edugma.domain.base.utils.TAG
import io.edugma.domain.base.utils.converters.LocalDateConverter
import io.edugma.domain.base.utils.converters.LocalDateTimeConverter
import io.edugma.domain.base.utils.converters.LocalTimeConverter
import io.edugma.domain.base.utils.converters.ZonedDateTimeConverter
import io.edugma.features.base.core.NativeText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime

abstract class Screen(
    val args: Map<String, String> = emptyMap(),
) {
    constructor(
        vararg args: Pair<String, Any?>,
    ) : this(args.filter { it.second != null }.associate { it.first to it.second.toArgString() })

    companion object {
        private fun <T> T.toArgString(): String {
            return when (this) {
                is String -> this
                is Int -> this.toString()
                is Long -> this.toString()
                is Float -> this.toString()
                is Double -> this.toString()
                is Screen -> ScreenInfoSerializer.serialize(this)
                is NativeText -> Json.encodeToString(this as NativeText)
                else -> this.toString()
            }
        }

        inline fun <reified T> T.serialized(): String? {
            if (this == null) return null
            return when (T::class) {
                LocalDate::class ->
                    Json.encodeToString(LocalDateConverter, this as LocalDate)
                LocalDateTime::class ->
                    Json.encodeToString(LocalDateTimeConverter, this as LocalDateTime)
                LocalTime::class ->
                    Json.encodeToString(LocalTimeConverter, this as LocalTime)
                ZonedDateTime::class ->
                    Json.encodeToString(ZonedDateTimeConverter, this as ZonedDateTime)
                else -> Json.encodeToString(this)
            }
        }
    }

    inline fun <reified T> getArg(key: String): T {
        return when (T::class) {
            String::class -> args[key] as T
            Int::class -> args[key]?.toInt() as T
            Long::class -> args[key]?.toLong() as T
            Float::class -> args[key]?.toFloat() as T
            Double::class -> args[key]?.toDouble() as T
            Screen::class -> args[key]?.let { ScreenInfoSerializer.deserialize(it) } as T
            LocalDate::class ->
                args[key]?.let { Json.decodeFromString(LocalDateConverter, it) } as T
            LocalDateTime::class ->
                args[key]?.let { Json.decodeFromString(LocalDateTimeConverter, it) } as T
            LocalTime::class ->
                args[key]?.let { Json.decodeFromString(LocalTimeConverter, it) } as T
            ZonedDateTime::class ->
                args[key]?.let { Json.decodeFromString(ZonedDateTimeConverter, it) } as T
            else -> args[key]?.let {
                try {
                    Json.decodeFromString<T>(it)
                } catch (e: Exception) {
                    Log.e(Screen::class.TAG, "Source: $it, type: ${T::class.qualifiedName}", e)
                    throw e
                }
            } as T
        }
    }

    inline fun <reified T> getArg(key: String, defaultValue: T): T {
        return getArg(key) ?: defaultValue
    }

    open val key = this::class.qualifiedName ?: ""
}
