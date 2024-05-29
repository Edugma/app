package com.edugma.core.api.utils

import com.benasher44.uuid.uuid4

object UUID {
    fun get(): String {
        return uuid4().toString()
    }
}
