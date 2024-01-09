package io.edugma.core.arch.mvi.viewmodel

import io.edugma.core.arch.mvi.MviErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler

interface CombinedErrorHandler : CoroutineExceptionHandler, MviErrorHandler
