package com.edugma.core.arch.mvi.viewmodel

import co.touchlab.kermit.Logger
import com.edugma.core.navigation.core.AccountRouter
import com.edugma.core.navigation.core.HomeRouter
import com.edugma.core.navigation.core.MiscRouter
import com.edugma.core.navigation.core.ScheduleRouter
import com.edugma.core.navigation.core.TabMenuRouter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.EmptyCoroutineContext

abstract class FeatureLogicDelegate<TState, TAction> : FeatureLogic<TState, TAction>() {
    override fun initialState(): TState {
        error("This is FeatureLogicDelegate")
    }
}

// TODO сделать бингдинг для передачи аргументов
abstract class FeatureLogic<TState, TAction> :
    FeatureStateMutator<TState>,
    FeatureActinLogic<TAction>,
    FeatureLifecycleLogic,
    KoinComponent {

    @Suppress("EmptyFunctionBlock")
    override fun onCreate() {}

    @Suppress("EmptyFunctionBlock")
    override fun onDestroy() {}

    fun create() {
        scope.launch {
            stateFlow.collect {
                Logger.d("State: $it", tag = TAG)
            }
        }
        // TODO продумать логику onCreate + делегаты
        onCreate()
        for (delegate in delegates) {
            delegate.create()
        }
    }

    fun destroy() {
        scope.cancel()
        onDestroy()
        for (delegate in delegates) {
            delegate.destroy()
        }
        delegates.clear()
    }

    private var _state: MutableFeatureState<TState>? = null
    private var _scope: CoroutineScope? = null
    private var _errorHandler: CombinedErrorHandler? = null

    val scope: CoroutineScope
        get() = _scope!!

    val errorHandler: CombinedErrorHandler?
        get() = _errorHandler

    override val state: TState
        get() = _state!!.value

    override val stateFlow: Flow<TState>
        get() = _state!!.flow

    val tabMenuRouter: TabMenuRouter by inject()
    val homeRouter: HomeRouter by inject()
    val scheduleRouter: ScheduleRouter by inject()
    val accountRouter: AccountRouter by inject()
    val miscRouter: MiscRouter by inject()

    protected val screenResultProvider: ScreenResultProvider by inject()

    protected fun newState(function: TState.() -> TState) {
        _state!!.update(function)
    }

    internal fun init(
        state: MutableFeatureState<TState>,
        scope: CoroutineScope,
        errorHandler: CombinedErrorHandler?,
    ) {
        _state = state
        _scope = scope
        _errorHandler = errorHandler
    }

    private val delegates = mutableListOf<FeatureLogic<*, *>>()

    fun <TDerived> addDelegate(
        logic: FeatureLogic<TDerived, *>,
        transform: (a: TState) -> TDerived,
        updateSource: TState.(TDerived) -> TState,
    ) {
        logic.init(
            _state!!.derideState(
                transform = transform,
                updateSource = updateSource,
            ),
            scope = _scope!!.copy(),
            errorHandler = _errorHandler,
        )
        delegates.add(logic)
    }

    private companion object {
        private const val TAG = "StateStore"
    }
}

@OptIn(ExperimentalStdlibApi::class)
private fun CoroutineScope.copy(): CoroutineScope {
    val dispatcher = this.coroutineContext.get(CoroutineDispatcher) ?: EmptyCoroutineContext
    return CoroutineScope(
        SupervisorJob() + dispatcher
    )
}

interface FeatureActinLogic<TAction> {
    fun processAction(action: TAction)
}

interface FeatureLifecycleLogic {
    fun onCreate()
    fun onDestroy()
}

interface FeatureStateMutator<T> {
    val state: T
    val stateFlow: Flow<T>
    fun initialState(): T
}
