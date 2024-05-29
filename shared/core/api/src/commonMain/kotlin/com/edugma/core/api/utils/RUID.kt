package com.edugma.core.api.utils

import kotlinx.atomicfu.atomic

/**
 * Runtime unique id.
 */
object RUID {
    private val counter = atomic(-1L)

    fun getString(): String {
        return counter.incrementAndGet().toString()
    }

    fun get(): Long {
        return counter.incrementAndGet()
    }
}
