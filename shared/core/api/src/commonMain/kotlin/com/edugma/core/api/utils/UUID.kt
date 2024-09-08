package com.edugma.core.api.utils

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object UUID {
    @OptIn(ExperimentalUuidApi::class)
    fun get(): String {
        return Uuid.random().toString()
    }
}
