package com.edugma.core.designSystem.organism.webView

import androidx.compose.runtime.Composable

@Composable
expect fun WebViewCompose(
    state: WebViewState,
    onFail: (errorCode: Int) -> Unit,
    onPageLoaded: () -> Unit,
)
