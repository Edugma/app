package io.edugma.core.arch.mvi.viewmodel

import co.touchlab.kermit.Logger
import io.edugma.core.arch.mvi.ActionProducer
import io.edugma.core.arch.mvi.BaseMutator
import io.edugma.core.arch.mvi.StateStore
import io.edugma.core.arch.mvi.impl.SimpleActionProducer
import io.edugma.core.arch.mvi.impl.SimpleStateStore
import io.edugma.core.arch.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModelWidgetFull<TState, TMutator : BaseMutator<TState>, TAction>(
    initialState: TState,
    private val mutatorFactory: () -> TMutator,
    private val stateStore: SimpleStateStore<TState, TMutator> =
        SimpleStateStore(initialState, mutatorFactory),
    private val actionProducer: SimpleActionProducer<TAction> =
        SimpleActionProducer(),
) : ViewModel(),
    StateStore<TState, TMutator> by stateStore,
    ActionProducer<TAction> by actionProducer {

    init {
        viewModelScope.launch(Dispatchers.Default) {
            state.collect {
                Logger.d(it.toString(), tag = "STATE")
            }
        }
    }
}
