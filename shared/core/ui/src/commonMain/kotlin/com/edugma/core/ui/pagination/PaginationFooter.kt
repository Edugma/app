package com.edugma.core.ui.pagination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.arch.pagination.PaginationUiState
import com.edugma.core.designSystem.atoms.loader.EdLoader
import com.edugma.core.designSystem.atoms.loader.EdLoaderSize
import com.edugma.core.designSystem.organism.refresher.Refresher

@Composable
fun PagingFooter(pagingState: PaginationUiState<*>, loadTrigger: () -> Unit) {
    when {
        pagingState.isContent() -> {
            Spacer(Modifier.height(70.dp))
            loadTrigger()
        }
        pagingState.isLoadingOrNeedLoading() -> Loader()
        pagingState.isError() -> Refresher(onClick = loadTrigger)
        else -> {}
    }
}

@Composable
private fun Loader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 70.dp),
    ) {
        EdLoader(
            modifier = Modifier
                .align(Alignment.Center),
            size = EdLoaderSize.medium,
        )
    }
}
