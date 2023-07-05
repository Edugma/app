package io.edugma.features.account.web

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
actual fun WebViewCompose(
    state: WebViewState,
    onFail: (errorCode: Int) -> Unit,
    onPageLoaded: () -> Unit,
) {
    Box {
        Text("WebView не поддерживается на ios")
    }
}
