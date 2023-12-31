package io.edugma.core.api.utils

import kotlinx.atomicfu.atomic

/**
 * Unique in runtime id.
 */
object RUID {
    private val counter = atomic(-1L)

    fun get(): String {
        return counter.incrementAndGet().toString()
    }
}
