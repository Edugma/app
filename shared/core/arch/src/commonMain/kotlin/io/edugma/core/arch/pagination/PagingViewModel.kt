package io.edugma.core.arch.pagination

import io.edugma.core.api.model.PagingDto
import io.edugma.core.arch.mvi.utils.launchCoroutine
import io.edugma.core.arch.mvi.viewmodel.ViewModelDelegate
import io.edugma.core.arch.mvi.viewmodel.newState
import kotlinx.coroutines.Job
import kotlin.properties.Delegates

class PagingViewModel<T> : ViewModelDelegate<PaginationState<T>>() {
    private var loadJob: Job? = null

    var request: (suspend () -> PagingDto<T>) by Delegates.notNull()

    // TODO Если быстро скроллить, то загрузка и всё
    fun resetAndLoad() {
        newState {
            toReset()
        }
        if (loadJob != null) {
            loadJob?.cancel()
        }
        loadJob = launchCoroutine(
            onError = {
                newState { toError() }
            },
        ) {
            val pagingDto = request()
            newState { toNewItems(pagingDto.data, pagingDto.next) }
        }
    }

    fun loadNextPage() {
        if (loadJob?.isActive == true) return
        newState { needLoadNext() }
        if (state.enum == PaginationStateEnum.NeedLoadNext) {
            loadJob = launchCoroutine(
                onError = {
                    newState { toError() }
                },
            ) {
                val pagingDto = request()
                newState { toNewItems(pagingDto.data, pagingDto.next) }
            }
        }
    }
}
