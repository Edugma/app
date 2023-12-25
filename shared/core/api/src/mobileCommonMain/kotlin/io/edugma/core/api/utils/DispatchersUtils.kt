package io.edugma.core.api.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import io.edugma.core.api.utils.IO

actual val Dispatchers.IO: CoroutineDispatcher
    get() = Dispatchers.IO
