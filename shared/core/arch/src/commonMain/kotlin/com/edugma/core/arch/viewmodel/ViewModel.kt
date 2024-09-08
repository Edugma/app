package com.edugma.core.arch.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "Don't use this api",
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
)
@Retention(AnnotationRetention.BINARY)
annotation class RestrictedApi

@Composable
inline fun <reified T : ViewModel> T.bind(crossinline onBind: () -> Unit) {
    DisposableEffect(key1 = this) {
        onBind()
        onDispose {
        }
    }
}
