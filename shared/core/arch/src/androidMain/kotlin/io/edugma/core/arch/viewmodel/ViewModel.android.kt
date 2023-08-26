package io.edugma.core.arch.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.koinInject
import androidx.lifecycle.ViewModel as AndroidxViewModel

@Composable
actual inline fun <reified T : ViewModel> getViewModel(): T {
    val defaultViewModel = viewModel<DefaultViewModel>()

    var viewModel = defaultViewModel.viewModels[T::class.qualifiedName] as? T

    if (viewModel == null) {
        viewModel = koinInject<T>()
        defaultViewModel.viewModels[T::class.qualifiedName ?: ""] = viewModel
    }


    DisposableEffect(key1 = viewModel) {
        onDispose {
            viewModel.clear()
        }
    }
    return viewModel
}

class DefaultViewModel : AndroidxViewModel() {
    val viewModels: MutableMap<String, ViewModel> = hashMapOf()

    override fun onCleared() {
        super.onCleared()
        viewModels.forEach { (_, viewModel) ->
            viewModel.clear()
        }
    }
}
