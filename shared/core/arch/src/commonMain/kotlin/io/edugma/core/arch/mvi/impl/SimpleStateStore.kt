package io.edugma.core.arch.mvi.impl

import io.edugma.core.arch.mvi.BaseMutator
import io.edugma.core.arch.mvi.MutatorObserver
import io.edugma.core.arch.mvi.StateStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SimpleStateStore<TState, TMutator : BaseMutator<TState>>(
    initialState: TState,
    private val mutatorFactory: () -> TMutator,
) : StateStore<TState, TMutator> {
    private val _state = MutableStateFlow(initialState)
    override val state = _state.asStateFlow()

    override fun getMutator(initialState: TState) = mutatorFactory().apply {
        this.state = initialState
        mutatorSetup()
    }

    override fun mutateState(mutate: TMutator.() -> Unit) {
        _state.value = getMutator(_state.value).apply {
            mutationScope {
                mutate()
            }
        }.state
    }

    private var mutatorSetup: TMutator.() -> Unit = { }

    override fun setupMutator(setup: MutatorObserver<TState, TMutator>.() -> Unit) {
        val observer = MutatorObserver<TState, TMutator>().apply(setup)
        mutatorSetup = {
            this.onStateChanged += {
                observer.notify(this)
            }
        }
    }
}
