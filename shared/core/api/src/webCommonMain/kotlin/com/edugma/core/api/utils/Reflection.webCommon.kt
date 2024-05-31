package com.edugma.core.api.utils

import kotlin.reflect.KClass

actual fun KClass<*>.getFullClassName(): String = this.simpleName ?: "KClass@${this.hashCode()}"
