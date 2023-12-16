package io.edugma.core.arch.mvi.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Deprecated("")
fun <T, Y> Flow<T>.prop(propSelector: T.() -> Y): Flow<Y> {
    return this.map { propSelector(it) }
        .distinctUntilChanged()
}

@Composable
fun <TAction> BaseActionViewModel<*, TAction>.rememberOnAction(): (TAction) -> Unit {
    return remember {
        { onAction(it) }
    }
}
