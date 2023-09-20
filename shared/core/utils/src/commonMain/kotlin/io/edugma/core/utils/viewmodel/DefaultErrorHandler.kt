package io.edugma.core.utils.viewmodel

import io.edugma.core.api.repository.MainSnackbarRepository
import io.edugma.core.api.utils.sendError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class DefaultErrorHandler(
    private val mainSnackbarRepository: MainSnackbarRepository,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        mainSnackbarRepository.sendError(exception)
    }
}
