package io.edugma.features.base.core.navigation.core

import co.touchlab.kermit.Logger
import io.edugma.domain.base.utils.TAG
import io.edugma.features.base.core.NativeText
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
            return Json.encodeToString(this)
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
                args[key]?.let { Json.decodeFromString<LocalDate>(it) } as T
            LocalDateTime::class ->
                args[key]?.let { Json.decodeFromString<LocalDateTime>(it) } as T
            LocalTime::class ->
                args[key]?.let { Json.decodeFromString<LocalTime>(it) } as T
            Instant::class ->
                args[key]?.let { Json.decodeFromString<Instant>(it) } as T
            else -> args[key]?.let {
                try {
                    Json.decodeFromString<T>(it)
                } catch (e: Exception) {
                    Logger.e("Source: $it, type: ${T::class.qualifiedName}", e, tag = Screen::class.TAG)
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
