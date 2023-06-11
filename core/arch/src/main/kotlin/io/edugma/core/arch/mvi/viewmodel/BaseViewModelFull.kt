package io.edugma.core.arch.mvi.viewmodel

import co.touchlab.kermit.Logger
import io.edugma.core.arch.mvi.ActionProducer
import io.edugma.core.arch.mvi.BaseMutator
import io.edugma.core.arch.mvi.StateStore
import io.edugma.core.arch.mvi.impl.SimpleActionProducer
import io.edugma.core.arch.mvi.impl.SimpleStateStore
import io.edugma.core.arch.viewmodel.ViewModel
import io.edugma.core.navigation.core.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseViewModelFull<TState, TMutator : BaseMutator<TState>, TAction>(
    initialState: TState,
    private val mutatorFactory: () -> TMutator,
    private val stateStore: SimpleStateStore<TState, TMutator> =
        SimpleStateStore(initialState, mutatorFactory),
    private val actionProducer: SimpleActionProducer<TAction> =
        SimpleActionProducer(),
) : ViewModel(),
    StateStore<TState, TMutator> by stateStore,
    ActionProducer<TAction> by actionProducer,
    KoinComponent {

    val router: Router by inject()

    protected val screenResultProvider: ScreenResultProvider by inject()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            state.collect {
                Logger.d(it.toString(), tag = "STATE")
            }
        }
    }

    open fun exit() {
        router.exit()
    }
}
