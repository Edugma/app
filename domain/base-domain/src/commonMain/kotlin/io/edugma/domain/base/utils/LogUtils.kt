package io.edugma.domain.base.utils

import kotlin.reflect.KClass

inline val <reified T> T.TAG: String
    get() = T::class.simpleName ?: ""

val <T : Any> KClass<T>.TAG: String
    get() = simpleName ?: ""
