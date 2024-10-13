package com.edugma.core.api.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class UpdateFlow(
    private val sharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(
        extraBufferCapacity = 1,
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
) : Flow<Unit> by sharedFlow {

    init {
        sharedFlow.tryEmit(Unit)
    }

    fun updated() {
        sharedFlow.tryEmit(Unit)
    }
}
