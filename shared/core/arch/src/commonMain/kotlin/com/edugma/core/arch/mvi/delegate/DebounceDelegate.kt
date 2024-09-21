package com.edugma.core.arch.mvi.delegate

import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DebounceDelegate(
    private val timeout: Long,
    val scope: CoroutineScope,
    val errorHandler: CoroutineExceptionHandler?,
) {
    private var lastJob: Job? = null

    fun CoroutineScope.launchDebounce(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        if (lastJob?.isActive == true) {
            lastJob?.cancel()
        }
        val job = launch(context, start) {
            delay(timeout)
            block()
        }
        lastJob = job

        return job
    }
}

fun FeatureLogic<*, *>.debounce(timeout: Long = DebounceConst.SEARCH_TIMEOUT): DebounceDelegate {
    return DebounceDelegate(
        timeout = timeout,
        scope = scope,
        errorHandler = errorHandler,
    )
}

object DebounceConst {
    const val SEARCH_TIMEOUT = 600L
}
