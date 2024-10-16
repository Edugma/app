package com.edugma.core.designSystem.organism.pullRefresh

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.surfaceColorAtElevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdPullRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier,
        state = state,
        isRefreshing = refreshing,
        onRefresh = {
            onRefresh()
        },
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = EdTheme.colorScheme.surfaceColorAtElevation(15.dp),
                color = EdTheme.colorScheme.onSurface,
                isRefreshing = refreshing,
                state = state
            )
        }
    ) {
        content()
    }
}
