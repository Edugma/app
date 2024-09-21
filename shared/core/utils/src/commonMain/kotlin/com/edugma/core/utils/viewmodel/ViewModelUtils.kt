package com.edugma.core.utils.viewmodel

import androidx.collection.MutableScatterMap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.node.Ref
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.edugma.core.api.utils.getFullClassName
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.arch.mvi.viewmodel.FeatureStore
import com.edugma.core.arch.mvi.viewmodel.buildFeatureStore
import org.koin.compose.koinInject

@Suppress("UNCHECKED_CAST")
@Composable
inline fun <TState, TAction, reified T : FeatureLogic<TState, TAction>> getViewModel(): T {
    val storeViewModel = viewModel { StoreViewModel() }

    val featureStoreRef = remember { Ref<FeatureStore<TState, TAction>>() }

    if (featureStoreRef.value == null) {
        val featureLogicKey = T::class.getFullClassName()
        featureStoreRef.value = storeViewModel.getOrPut(featureLogicKey) {
            val newFeatureLogic = koinInject<T>()
            val defaultErrorHandler = koinInject<DefaultErrorHandler>()
            val store = buildFeatureStore(
                logic = newFeatureLogic,
                errorHandler = defaultErrorHandler,
            )
            store.create()
            store
        } as FeatureStore<TState, TAction>
    }

    return featureStoreRef.value!!.logic as T
}

public class StoreViewModel : ViewModel() {
    private val map = MutableScatterMap<String, FeatureStore<*, *>>()
    fun get(key: String): FeatureStore<*, *>? {
        return map.get(key)
    }

    fun put(key: String, value: FeatureStore<*, *>) {
        map.put(key, value)
    }

    override fun onCleared() {
        super.onCleared()
        map.forEachValue {
            it.destroy()
        }
    }

    public inline fun getOrPut(key: String, defaultValue: () -> FeatureStore<*, *>): FeatureStore<*, *> {
        val value = get(key)
        return if (value == null) {
            val answer = defaultValue()
            put(key, answer)
            answer
        } else {
            value
        }
    }
}

@Composable
fun <TState> FeatureLogic<TState, *>.collectAsState(): State<TState> {
    return this.stateFlow.collectAsState(initial = this.state)
}
