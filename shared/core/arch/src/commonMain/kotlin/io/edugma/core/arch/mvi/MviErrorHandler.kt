package io.edugma.core.arch.mvi

interface MviErrorHandler {
    fun handleException(exception: Throwable)
}
