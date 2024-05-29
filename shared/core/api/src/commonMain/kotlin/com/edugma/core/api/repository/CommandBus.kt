package com.edugma.core.api.repository

import kotlinx.coroutines.flow.SharedFlow

interface CommandBus<T> {
    val messageFlow: SharedFlow<T>
}
