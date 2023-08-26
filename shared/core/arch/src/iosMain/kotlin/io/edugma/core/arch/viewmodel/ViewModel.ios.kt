package io.edugma.core.arch.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import org.koin.compose.koinInject

@Composable
actual inline fun <reified T : ViewModel> getViewModel(): T {
    val viewModel = koinInject<T>()
    DisposableEffect(key1 = viewModel) {
        onDispose {
            viewModel.clear()
        }
    }
    return viewModel
}

