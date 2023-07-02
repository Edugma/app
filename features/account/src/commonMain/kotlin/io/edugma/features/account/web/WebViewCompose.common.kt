package io.edugma.features.account.web

import androidx.compose.runtime.Composable

@Composable
expect fun WebViewCompose(
    state: WebViewState,
    onFail: (errorCode: Int) -> Unit,
    onPageLoaded: () -> Unit,
)
