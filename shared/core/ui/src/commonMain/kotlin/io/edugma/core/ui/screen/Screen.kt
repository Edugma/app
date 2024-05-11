package io.edugma.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.organism.bottomSheet.EdModalBottomSheet
import io.edugma.core.designSystem.organism.bottomSheet.SheetState
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.utils.ifThen

@Composable
fun FeatureScreen(
    statusBarPadding: Boolean = true,
    navigationBarPadding: Boolean = true,
    isNavigationBarVisible: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .ifThen(statusBarPadding) {
                this.statusBarsPadding()
            }.ifThen(navigationBarPadding) {
                this.navigationBarsPadding()
            },
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureBottomSheetScreen(
    statusBarPadding: Boolean = true,
    navigationBarPadding: Boolean = true,
    imePadding: Boolean = true,
    isNavigationBarVisible: Boolean = true,
    sheetNavigationBarPadding: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val imeInsets = if (imePadding) {
        WindowInsets.ime
    } else {
        WindowInsets(0)
    }
    val navigationBarInsets = if (sheetNavigationBarPadding) {
        WindowInsets.navigationBars
    } else {
        WindowInsets(0)
    }
    Box(
        modifier = Modifier.fillMaxSize()
            .ifThen(statusBarPadding) {
                statusBarsPadding()
            }.ifThen(navigationBarPadding) {
                navigationBarsPadding()
            },
    ) {
        content()
        EdModalBottomSheet(
            sheetState = sheetState,
            modifier = Modifier.ifThen(imePadding) {
                this.imePadding()
            },
            windowInsets = imeInsets.union(navigationBarInsets),
        ) {
            sheetContent()
        }
    }
}
