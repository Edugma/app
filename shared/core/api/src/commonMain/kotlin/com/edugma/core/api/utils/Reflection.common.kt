package com.edugma.core.api.utils

import kotlin.reflect.KClass

/**
 * Return full class name.
 */
expect fun KClass<*>.getFullClassName(): String
