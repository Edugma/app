package com.edugma.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import co.touchlab.kermit.Logger
import com.edugma.core.designSystem.organism.bottomSheet.EdModalBottomSheet
import com.edugma.core.designSystem.organism.bottomSheet.SheetState
import com.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import com.edugma.core.designSystem.utils.ifThen

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

@Composable
fun FeatureBottomSheetScreen(
    statusBarPadding: Boolean = true,
    navigationBarPadding: Boolean = true,
    imePadding: Boolean = true,
    isNavigationBarVisible: Boolean = true,
    sheetNavigationBarPadding: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetContent: @Composable ColumnScope.() -> Unit,
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
    if (sheetState.showBottomSheet) {
        EdModalBottomSheet(
            onDismissRequest = {
                sheetState.setHide()
            },
            sheetState = sheetState,
            windowInsets = imeInsets,
        ) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(navigationBarInsets)
                    .fillMaxWidth(),
            ) {
                sheetContent()
            }
        }
    }
}
