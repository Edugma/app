package com.edugma.core.api.repository

import com.edugma.core.api.model.AppState
import kotlinx.coroutines.flow.StateFlow

interface AppStateRepository {
    val state: StateFlow<AppState>
    fun newState(state: AppState)
}
