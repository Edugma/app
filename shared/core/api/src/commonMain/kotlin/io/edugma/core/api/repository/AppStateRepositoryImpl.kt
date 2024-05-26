package io.edugma.core.api.repository

import co.touchlab.kermit.Logger
import io.edugma.core.api.model.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AppStateRepositoryImpl : AppStateRepository {

    private val _state = MutableStateFlow(AppState())
    override val state: StateFlow<AppState> = _state.asStateFlow()

    override fun newState(state: AppState) {
        Logger.d("New AppState $state", tag = "AppStateRepository")
        _state.value = state
    }
}
