package io.edugma.features.base.core.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ScreenResultProvider {

    private val events = MutableSharedFlow<ScreenResult>(replay = 0)

    fun getEvents(): SharedFlow<ScreenResult> = events

    suspend fun sendEvent(result: ScreenResult) {
        events.emit(result)
    }

}

interface ScreenResult