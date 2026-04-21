package com.edugma.navigation.core.destination

import kotlin.reflect.KClass

interface ArgumentsStore {
    operator fun <T : Any> get(key: String, clazz: KClass<T>): T?
}
