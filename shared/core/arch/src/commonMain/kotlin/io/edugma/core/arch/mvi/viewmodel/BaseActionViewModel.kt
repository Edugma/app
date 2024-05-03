package io.edugma.core.arch.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import io.edugma.core.arch.mvi.ActionProducer
import io.edugma.core.arch.mvi.StateStore
import io.edugma.core.arch.mvi.impl.SimpleStateStore
import io.edugma.core.arch.viewmodel.RestrictedApi
import io.edugma.core.navigation.core.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseActionViewModel<TState, TAction>(
    initialState: TState,
    private val stateStore: SimpleStateStore<TState> = SimpleStateStore(initialState),
) : ViewModel(),
    StateStore<TState> by stateStore,
    ActionProducer<TAction>,
    KoinComponent {

    val router: Router by inject()

    internal var _errorHandler: CombinedErrorHandler? = null
    val errorHandler: CombinedErrorHandler?
        get() = _errorHandler

    protected val screenResultProvider: ScreenResultProvider by inject()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            stateFlow.collect {
                Logger.d(it.toString(), tag = "STATE")
            }
        }
    }

    open fun exit() {
        router.back()
    }
}
