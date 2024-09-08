package com.edugma.core.arch.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.edugma.core.api.utils.IO
import com.edugma.core.arch.mvi.ActionProducer
import com.edugma.core.arch.mvi.StateStore
import com.edugma.core.arch.mvi.impl.SimpleStateStore
import com.edugma.core.navigation.core.AccountRouter
import com.edugma.core.navigation.core.HomeRouter
import com.edugma.core.navigation.core.MiscRouter
import com.edugma.core.navigation.core.ScheduleRouter
import com.edugma.core.navigation.core.TabMenuRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseActionViewModel<TState, TAction>(
    initialState: TState,
    private val stateStore: SimpleStateStore<TState> = SimpleStateStore(initialState),
) : ViewModel(),
    StateStore<TState> by stateStore,
    ActionProducer<TAction>,
    KoinComponent {

    val tabMenuRouter: TabMenuRouter by inject()
    val homeRouter: HomeRouter by inject()
    val scheduleRouter: ScheduleRouter by inject()
    val accountRouter: AccountRouter by inject()
    val miscRouter: MiscRouter by inject()

    @Suppress("VariableNaming")
    internal var _errorHandler: CombinedErrorHandler? = null
    val errorHandler: CombinedErrorHandler?
        get() = _errorHandler

    final override fun onAction(action: TAction) {
        Logger.d("Action: $action", tag = TAG)
        processAction(action)
    }

    open fun processAction(action: TAction) {
    }

    protected val screenResultProvider: ScreenResultProvider by inject()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            stateFlow.collect {
                Logger.d("State: $it", tag = TAG)
            }
        }
    }

    private companion object {
        private const val TAG = "StateStore"
    }
}
