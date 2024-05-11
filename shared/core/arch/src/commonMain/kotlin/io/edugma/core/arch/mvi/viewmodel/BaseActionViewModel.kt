package io.edugma.core.arch.mvi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import io.edugma.core.arch.mvi.ActionProducer
import io.edugma.core.arch.mvi.StateStore
import io.edugma.core.arch.mvi.impl.SimpleStateStore
import io.edugma.core.navigation.core.AccountRouter
import io.edugma.core.navigation.core.HomeRouter
import io.edugma.core.navigation.core.MiscRouter
import io.edugma.navigation.core.router.Router
import io.edugma.core.navigation.core.ScheduleRouter
import io.edugma.core.navigation.core.TabMenuRouter
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

    internal var _errorHandler: CombinedErrorHandler? = null
    val errorHandler: CombinedErrorHandler?
        get() = _errorHandler

    protected val screenResultProvider: ScreenResultProvider by inject()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            stateFlow.collect {
                Logger.d(it.toString(), tag = "STATE")
            }
        }
    }
}
