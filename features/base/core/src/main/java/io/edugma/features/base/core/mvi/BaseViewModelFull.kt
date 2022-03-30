package io.edugma.features.base.core.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.edugma.features.base.core.mvi.impl.SimpleActionProducer
import io.edugma.features.base.core.mvi.impl.SimpleStateStore
import io.edugma.features.base.core.navigation.core.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModelFull<TState, TMutator : BaseMutator<TState>, TAction>(
    initialState: TState,
    private val mutatorFactory: () -> TMutator,
    private val stateStore: SimpleStateStore<TState, TMutator>
    = SimpleStateStore(initialState,  mutatorFactory),
    private val actionProducer: SimpleActionProducer<TAction>
    = SimpleActionProducer()
): ViewModel(),
    StateStore<TState, TMutator> by stateStore,
    ActionProducer<TAction> by actionProducer,
    KoinComponent {

    val router: Router by inject()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            state.collect {
                Log.d("STATE", it.toString())
            }
        }
    }

    open fun exit() {
        router.exit()
    }
}