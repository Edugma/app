package io.edugma.features.account.web

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.organism.errorWithRetry.ErrorWithRetry
import io.edugma.core.designSystem.organism.topAppBar.EdTopAppBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.ui.screen.FeatureScreen
import io.edugma.core.utils.isNotNull

@Composable
fun WebScreen(
    url: String,
    isFullScreen: Boolean,
    viewModel: WebViewModel = getViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(url, isFullScreen)
    }

    FeatureScreen {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!state.isFullScreen) {
                EdTopAppBar("", onNavigationClick = viewModel::exit)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { context ->
                        WebViewView(context).apply {
                            failListener = { errorCode, _ -> viewModel.loadingError(errorCode) }
                            pageLoadedListener = viewModel::loadingEnd
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { view ->
                        state.authToken?.let { view.authToken = it }
                        state.url
                            ?.takeIf { it != view.url }
                            ?.let(view::loadUrl)
                        view.visibility = if (state.isLoading || state.isWebViewLoading) {
                            INVISIBLE
                        } else {
                            VISIBLE
                        }
                    },
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
                        ErrorWithRetry(
                            modifier = Modifier.background(EdTheme.colorScheme.background).fillMaxSize(),
                            retryAction = { viewModel.init(url, isFullScreen) },
                        )
                    }
                }
            }
        }
    }
}
