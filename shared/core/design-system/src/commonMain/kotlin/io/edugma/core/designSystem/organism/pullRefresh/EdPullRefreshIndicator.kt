package io.edugma.core.designSystem.organism.pullRefresh

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.surfaceColorAtElevation

@OptIn(ExperimentalMaterialApi::class)
@Composable
@NonRestartableComposable
fun EdPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier,
) {
    PullRefreshIndicator(
        refreshing = refreshing,
        state = state,
        modifier = modifier,
        backgroundColor = EdTheme.colorScheme.surfaceColorAtElevation(15.dp),
        contentColor = EdTheme.colorScheme.onSurface,
        scale = true,
    )
}
