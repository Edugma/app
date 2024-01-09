package io.edugma.core.designSystem.organism.lceScaffold

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
import io.edugma.core.api.model.LceUiState
import io.edugma.core.designSystem.organism.errorWithRetry.EdErrorRetry
import io.edugma.core.designSystem.organism.nothingFound.EdNothingFound
import io.edugma.core.designSystem.organism.pullRefresh.EdPullRefresh

// TODO replace multiple booleans with enum
@Composable
fun EdLceScaffold(
    lceState: LceUiState,
    emptyTitle: String = "К сожалению, ничего не найдено",
    onRefresh: () -> Unit,
    insets: WindowInsets = WindowInsets.navigationBars,
    placeholder: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    EdPullRefresh(refreshing = lceState.isRefreshing, onRefresh = onRefresh) {
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
}
