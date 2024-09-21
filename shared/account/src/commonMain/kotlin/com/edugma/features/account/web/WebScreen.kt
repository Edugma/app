package com.edugma.features.account.web

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.atoms.loader.EdLoader
import com.edugma.core.designSystem.organism.errorWithRetry.EdErrorRetry
import com.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import com.edugma.core.designSystem.organism.webView.WebViewCompose
import com.edugma.core.designSystem.organism.webView.WebViewState
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.ui.screen.FeatureScreen
import com.edugma.core.utils.isNotNull
import com.edugma.core.utils.viewmodel.collectAsState
import com.edugma.core.utils.viewmodel.getViewModel

@Composable
fun WebScreen(
    url: String,
    isFullScreen: Boolean,
    viewModel: WebViewModel = getViewModel(),
) {
    val state by viewModel.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(url, isFullScreen)
    }

    FeatureScreen {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!state.isFullScreen) {
                EdTopAppBar("", onNavigationClick = viewModel::exit)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                WebViewCompose(
                    state = WebViewState(
                        token = state.authToken,
                        url = state.url,
                        isVisible = state.isLoading || state.isWebViewLoading,
                    ),
                    onFail = { errorCode -> viewModel.loadingError(errorCode) },
                    onPageLoaded = viewModel::loadingEnd,
                )
                when {
                    state.isLoading || state.isWebViewLoading -> {
                        Box(
                            modifier = Modifier
                                .background(EdTheme.colorScheme.background)
                                .fillMaxSize(),
                        ) {
                            EdLoader(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                    state.errorCode.isNotNull() -> {
                        EdErrorRetry(
                            modifier = Modifier.background(EdTheme.colorScheme.background).fillMaxSize(),
                            onRetry = { viewModel.init(url, isFullScreen) },
                        )
                    }
                }
            }
        }
    }
}
