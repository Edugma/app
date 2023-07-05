package io.edugma.core.arch.mvi

import kotlinx.coroutines.flow.StateFlow

interface StateStore<TState, TMutator : BaseMutator<TState>> {
    val state: StateFlow<TState>

    fun setupMutator(setup: MutatorObserver<TState, TMutator>.() -> Unit)
    fun getMutator(initialState: TState): TMutator
    fun mutateState(mutate: TMutator.() -> Unit)
}
