package com.edugma.core.designSystem.organism.webView

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun WebViewCompose(
    state: WebViewState,
    onFail: (errorCode: Int) -> Unit,
    onPageLoaded: () -> Unit,
) {
    AndroidView(
        factory = { context ->
            WebViewView(context).apply {
                failListener = { errorCode, _ -> onFail(errorCode) }
                pageLoadedListener = onPageLoaded
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
            state.token?.let { view.authToken = it }
            state.url
                ?.takeIf { it != view.url }
                ?.let(view::loadUrl)
            view.visibility = if (state.isVisible) {
                INVISIBLE
            } else {
                VISIBLE
            }
        },
    )
}
