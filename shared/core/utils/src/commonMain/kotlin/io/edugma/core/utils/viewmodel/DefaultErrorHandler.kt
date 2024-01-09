package io.edugma.core.utils.viewmodel

import io.edugma.core.api.repository.MainSnackbarRepository
import io.edugma.core.api.utils.sendError
import io.edugma.core.arch.mvi.viewmodel.CombinedErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class DefaultErrorHandler(
    private val mainSnackbarRepository: MainSnackbarRepository,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CombinedErrorHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        mainSnackbarRepository.sendError(exception)
    }

    override fun handleException(exception: Throwable) {
        mainSnackbarRepository.sendError(exception)
    }
}
