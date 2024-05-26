package io.edugma.core.api.repository

import io.edugma.core.api.model.AppState
import kotlinx.coroutines.flow.StateFlow

interface AppStateRepository {
    val state: StateFlow<AppState>
    fun newState(state: AppState)
}
