package com.edugma.core.arch.mvi.viewmodel

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ScreenResultProvider {

    private val events = MutableSharedFlow<ScreenResult>(replay = 0)

    fun getEvents(): SharedFlow<ScreenResult> = events

    suspend fun sendEvent(result: ScreenResult) {
        events.emit(result)
    }
}

interface ScreenResult
