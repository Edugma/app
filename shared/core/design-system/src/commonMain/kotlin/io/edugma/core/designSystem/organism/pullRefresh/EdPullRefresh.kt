package io.edugma.core.designSystem.organism.pullRefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.surfaceColorAtElevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdPullRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val refreshingState = rememberUpdatedState(refreshing)
    val state = rememberPullToRefreshState(
        enabled = {
            refreshingState.value.not()
        }
    )

    LaunchedEffect(Unit) {
        snapshotFlow { refreshingState.value }.collect {
            if (it) {
                state.startRefresh()
            } else {
                state.endRefresh()
            }
        }
    }

    LaunchedEffect(state) {
        snapshotFlow { state.isRefreshing }.collect {
            if (state.isRefreshing && state.isRefreshing != refreshingState.value) {
                onRefresh()
            }
        }
    }

    Box(
        modifier = modifier.nestedScroll(state.nestedScrollConnection)
            .clip(RectangleShape),
    ) {
        content()
        PullToRefreshContainer(
            state = state,
            containerColor = EdTheme.colorScheme.surfaceColorAtElevation(15.dp),
            contentColor = EdTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.TopCenter),
        )
    }
}
