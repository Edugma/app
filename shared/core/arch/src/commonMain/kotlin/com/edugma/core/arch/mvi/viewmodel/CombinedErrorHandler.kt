package com.edugma.core.arch.mvi.viewmodel

import com.edugma.core.arch.mvi.MviErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler

interface CombinedErrorHandler : CoroutineExceptionHandler, MviErrorHandler
