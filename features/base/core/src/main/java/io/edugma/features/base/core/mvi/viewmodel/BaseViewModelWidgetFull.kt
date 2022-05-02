package io.edugma.features.base.core.mvi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.edugma.features.base.core.mvi.ActionProducer
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.features.base.core.mvi.StateStore
import io.edugma.features.base.core.mvi.impl.SimpleActionProducer
import io.edugma.features.base.core.mvi.impl.SimpleStateStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class BaseViewModelWidgetFull<TState, TMutator : BaseMutator<TState>, TAction>(
    initialState: TState,
    private val mutatorFactory: () -> TMutator,
    private val stateStore: SimpleStateStore<TState, TMutator>
    = SimpleStateStore(initialState,  mutatorFactory),
    private val actionProducer: SimpleActionProducer<TAction>
    = SimpleActionProducer()
): ViewModel(),
    StateStore<TState, TMutator> by stateStore,
    ActionProducer<TAction> by actionProducer
{

    init {
        viewModelScope.launch(Dispatchers.Default) {
            state.collect {
                Log.d("STATE", it.toString())
            }
        }
    }
}