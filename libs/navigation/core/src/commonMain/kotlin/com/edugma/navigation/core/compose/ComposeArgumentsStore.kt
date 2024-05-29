package com.edugma.navigation.core.compose

import androidx.core.bundle.Bundle
import com.edugma.navigation.core.destination.ArgumentsStore

class ComposeArgumentsStore(
    private val arguments: Bundle,
) : ArgumentsStore {
    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun <T> get(key: String): T? {
        return arguments.get(key) as T
    }
}

