package com.edugma.navigation.core.compose

import androidx.savedstate.SavedState
import androidx.savedstate.read
import com.edugma.navigation.core.destination.ArgumentsStore
import kotlin.reflect.KClass

class ComposeArgumentsStore(
    private val arguments: SavedState,
) : ArgumentsStore {
    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun <T : Any> get(key: String, clazz: KClass<T>): T? {
        return arguments.read {
            when (clazz) {
                String::class -> getString(key)
                Boolean::class -> getBoolean(key)
                Int::class -> getInt(key)
                Long::class -> getLong(key)
                Float::class -> getFloat(key)
                Double::class -> getDouble(key)
                else -> error("Unknown argument type: ${clazz.qualifiedName}")
            }
        } as T
    }
}
