package io.edugma.features.account.domain.usecase

import io.edugma.core.api.model.PagingDTO
import io.edugma.core.arch.mvi.viewmodel.BaseActionViewModel
import io.edugma.core.utils.Typed1Listener
import io.edugma.core.utils.Typed2Listener
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class PagingUseCase<T>(
    firstPage: Int = 1,
    private val request: suspend (page: Int) -> Result<PagingDTO<T>>,
) {

    private val paginationStateFlow = MutableStateFlow(PaginationState.NotLoading)

    var page = firstPage
        private set

    var isEnded: Boolean = false
        private set

    val paginationState
        get() = paginationStateFlow.asStateFlow()

    suspend fun getData(): Result<List<T>> {
        if (isEnded) return Result.success(emptyList())
        paginationStateFlow.emit(PaginationState.Loading)
        return request.invoke(page)
            .onSuccess {
                isEnded = it.nextPage.isNull() || it.data.isEmpty()
                page++
                if (isEnded) {
                    paginationStateFlow.emit(PaginationState.End)
                } else {
                    paginationStateFlow.emit(PaginationState.Loaded)
                }
            }
            .onFailure { paginationStateFlow.emit(PaginationState.Error) }
            .map { it.data }
    }

    suspend fun reset() {
        page = 1
        paginationStateFlow.emit(PaginationState.NotLoading)
    }
}

enum class PaginationState {
    NotLoading, Loading, Loaded, End, Error
}

interface PagingViewModel<T> {

    var pagingUC: PagingUseCase<T>

    var pagingStateJob: Job?
    var loadingJob: Job?

    val onDataLoaded: Typed2Listener<List<T>, Boolean>?
    val onLoadingStateChange: Typed1Listener<PaginationState>?
    val onError: Typed1Listener<Throwable>?

    fun BaseActionViewModel<*, *>.updateUc(firstPage: Int = 1, request: suspend (page: Int) -> Result<PagingDTO<T>>) {
        pagingUC = PagingUseCase(firstPage, request)
        loadingJob?.cancel()
        pagingStateJob?.cancel()
        pagingStateJob = launchCoroutine {
            pagingUC.paginationState.onEach { onLoadingStateChange?.invoke(it) }.collect()
        }
        loadingJob = launchCoroutine {
            pagingUC.getData()
                .onSuccess { onDataLoaded?.invoke(it, true) }
                .onFailure { onError?.invoke(it) }
        }
    }

    fun BaseActionViewModel<*, *>.loadNext(fromBegin: Boolean = false) {
        if (loadingJob?.isActive == true) return
        loadingJob = launchCoroutine {
            pagingUC.getData()
                .onSuccess {
                    if (it.isNotEmpty()) onDataLoaded?.invoke(it, fromBegin)
                }
                .onFailure { onError?.invoke(it) }
        }
    }
}
