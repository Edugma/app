package com.edugma.core.designSystem.organism.webView

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.edugma.core.designSystem.organism.webView.WebViewState

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
