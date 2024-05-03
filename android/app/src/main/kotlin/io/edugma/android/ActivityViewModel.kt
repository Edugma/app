package io.edugma.android

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
fun ProvideActivityViewModelStoreOwner(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalViewModelStoreOwner provides LocalContext.current.getActivity(),
        content = content,
    )
}

private fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
