package com.edugma.core.arch.mvi.viewmodel

import androidx.lifecycle.viewModelScope
import com.edugma.core.arch.mvi.StateStore
import com.edugma.core.arch.viewmodel.RestrictedApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.properties.Delegates

abstract class ViewModelDelegate<TState> : StateStore<TState> {
    private var _stateFlow: StateFlow<TState> by Delegates.notNull()
    private var _setState: (TState) -> Unit by Delegates.notNull()

    @RestrictedApi
    internal var viewModelScope: CoroutineScope by Delegates.notNull()
        private set

    override val state: TState
        get() = _stateFlow.value

    @RestrictedApi
    override fun setState(state: TState) {
        _setState(state)
    }

    override val stateFlow: StateFlow<TState>
        get() = _stateFlow

    private var _errorHandler: CoroutineExceptionHandler? = null
    val errorHandler: CoroutineExceptionHandler?
        get() = _errorHandler

    @OptIn(RestrictedApi::class)
    fun init(
        parentViewModel: BaseActionViewModel<*, *>,
        initialState: TState,
        stateFlow: Flow<TState>,
        setState: (TState) -> Unit,
    ) {
        _setState = setState
        this.viewModelScope = parentViewModel.viewModelScope
        _errorHandler = parentViewModel.errorHandler
        _stateFlow = stateFlow.stateIn(viewModelScope, SharingStarted.Eagerly, initialState)
    }
}

@OptIn(RestrictedApi::class)
inline fun <TState> ViewModelDelegate<TState>.newState(
    newState: TState.() -> TState,
) {
    setState(newState(state))
}
