package io.edugma.features.base.core.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.onPageChanged(action: suspend (page: Int) -> Unit) {
    LaunchedEffect(this) {
        snapshotFlow { currentPage }.collect { page ->
            action(page)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerState.bindTo(position: Int, animated: Boolean = false) {
    LaunchedEffect(position, animated) {
        if (currentPage != position) {
            if (animated) {
                animateScrollToPage(position)
            } else {
                scrollToPage(position)
            }
        }
    }
}

// @OptIn(ExperimentalPagerApi::class)
// @Composable
// fun PagerState.onPageChanged(action: suspend (page: Int) -> Unit) {
//    LaunchedEffect(this) {
//        snapshotFlow { calculatePage(currentPage, currentPageOffset) }
//            .distinctUntilChanged()
//            .collect { page ->
//                action(page)
//            }
//    }
// }
//
// private fun calculatePage(page: Int, offset: Float): Int {
//    return when {
//        offset < -0.5f -> page - 1
//        offset < 0.5f -> page
//        else -> page + 1
//    }
// }
//
// @OptIn(ExperimentalPagerApi::class)
// @Composable
// fun PagerState.bindTo(position: Int, animated: Boolean = false) {
//    LaunchedEffect(position, animated) {
//        val oldPos = calculatePage(currentPage, currentPageOffset)
//        if (oldPos != position) {
//            if (animated) {
//                animateScrollToPage(position)
//            } else {
//                scrollToPage(position)
//            }
//        }
//    }
// }
