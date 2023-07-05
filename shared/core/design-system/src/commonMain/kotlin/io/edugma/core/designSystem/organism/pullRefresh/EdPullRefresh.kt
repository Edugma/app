package io.edugma.core.designSystem.organism.pullRefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
inline fun EdPullRefresh(
    refreshing: Boolean,
    noinline onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, onRefresh)

    Box(
        modifier = modifier.pullRefresh(pullRefreshState),
    ) {
        content()
        EdPullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
