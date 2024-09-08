package com.edugma.core.designSystem.organism.lceScaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.api.model.LceUiState
import com.edugma.core.designSystem.organism.errorWithRetry.EdErrorRetry
import com.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import com.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh

// TODO replace multiple booleans with enum
@Composable
fun EdLceScaffold(
    lceState: LceUiState,
    emptyTitle: String = "К сожалению, ничего не найдено",
    onRefresh: () -> Unit,
    pullToRefreshEnabled: Boolean = true,
    insets: WindowInsets = WindowInsets.navigationBars,
    placeholder: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    if (pullToRefreshEnabled) {
        EdPullRefresh(
            refreshing = lceState.isRefreshing,
            onRefresh = onRefresh,
        ) {
            EdLceScaffoldContent(
                lceState = lceState,
                emptyTitle = emptyTitle,
                onRefresh = onRefresh,
                insets = insets,
                placeholder = placeholder,
                content = content,
            )
        }
    } else {
        Box(Modifier.fillMaxSize()) {
            EdLceScaffoldContent(
                lceState = lceState,
                emptyTitle = emptyTitle,
                onRefresh = onRefresh,
                insets = insets,
                placeholder = placeholder,
                content = content,
            )
        }
    }
}

@Composable
private fun EdLceScaffoldContent(
    lceState: LceUiState,
    emptyTitle: String,
    onRefresh: () -> Unit,
    insets: WindowInsets,
    placeholder: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    when {
        lceState.showError -> {
            EdErrorRetry(
                modifier = Modifier.fillMaxSize()
                    .consumeWindowInsets(insets),
                onRetry = onRefresh,
            )
        }

        lceState.showEmptyContent -> {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
                    .consumeWindowInsets(insets),
            ) {
                EdNothingFound(
                    modifier = Modifier.fillMaxWidth()
                        .height(maxHeight)
                        .verticalScroll(rememberScrollState()),
                    title = emptyTitle,
                )
            }
        }

        lceState.showPlaceholder -> {
            placeholder()
        }

        else -> {
            content()
        }
    }
}
