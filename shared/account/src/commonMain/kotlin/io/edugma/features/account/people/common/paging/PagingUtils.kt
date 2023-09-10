package io.edugma.features.account.people.common.paging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.atoms.loader.EdLoaderSize
import io.edugma.core.designSystem.organism.refresher.Refresher
import io.edugma.core.utils.ClickListener
import io.edugma.features.account.domain.usecase.PaginationState

@Composable
fun PagingFooter(pagingState: PaginationState, loadTrigger: ClickListener) {
    when (pagingState) {
        PaginationState.Loaded -> {
            Spacer(Modifier.height(70.dp))
            loadTrigger()
        }
        PaginationState.Loading -> Loader()
        PaginationState.Error -> Refresher(onClick = loadTrigger)
        PaginationState.End, PaginationState.NotLoading -> {}
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
