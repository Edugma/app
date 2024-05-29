package com.edugma.core.arch.mvi

interface MviErrorHandler {
    fun handleException(exception: Throwable)
}
