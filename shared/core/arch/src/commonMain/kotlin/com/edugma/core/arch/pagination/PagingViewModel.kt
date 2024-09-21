package com.edugma.core.arch.pagination

import co.touchlab.kermit.Logger
import com.edugma.core.api.model.PagingDto
import com.edugma.core.arch.mvi.utils.launchCoroutine
import com.edugma.core.arch.mvi.viewmodel.FeatureLogicDelegate
import kotlinx.coroutines.Job
import kotlin.properties.Delegates

class PagingViewModel<T> : FeatureLogicDelegate<PaginationUiState<T>, Nothing>() {
    private var loadJob: Job? = null

    @Suppress("EmptyFunctionBlock")
    override fun processAction(action: Nothing) {
    }

    var request: (suspend () -> PagingDto<T>) by Delegates.notNull()

    // TODO Если быстро скроллить, то загрузка и всё
    fun resetAndLoad() {
        Logger.d("Pagination: reset and load pagination", tag = TAG)
        newState {
            toReset()
        }
        load(append = true)
    }

    fun refresh() {
        Logger.d("Pagination: refresh", tag = TAG)
        newState {
            toRefresh()
        }
        load(append = false)
    }

    private fun load(append: Boolean) {
        if (loadJob != null) {
            loadJob?.cancel()
        }
        loadJob = launchCoroutine(
            onError = {
                newState { toError() }
            },
        ) {
            val pagingDto = request()
            if (append) {
                newState { addItems(pagingDto.data, pagingDto.next) }
            } else {
                newState { replaceItems(pagingDto.data, pagingDto.next) }
            }
        }
    }

    fun loadNextPage() {
        Logger.d("Pagination: load next page", tag = TAG)
        if (loadJob?.isActive == true) return
        newState { needLoadNext() }
        if (state.enum == PaginationStateEnum.NeedLoadNext) {
            loadJob = launchCoroutine(
                onError = {
                    newState { toError() }
                },
            ) {
                val pagingDto = request()
                newState { addItems(pagingDto.data, pagingDto.next) }
            }
        }
    }

    companion object {
        private const val TAG = "StateStore"
    }
}
