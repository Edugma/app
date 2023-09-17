package io.edugma.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.union
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.organism.bottomSheet.EdModalBottomSheet
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetState
import io.edugma.core.designSystem.organism.bottomSheet.ModalBottomSheetValue
import io.edugma.core.designSystem.organism.bottomSheet.rememberModalBottomSheetState
import io.edugma.core.designSystem.utils.ifThen
import io.edugma.core.designSystem.utils.imePadding
import io.edugma.core.designSystem.utils.navigationBarsPadding
import io.edugma.core.designSystem.utils.statusBarsPadding

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
                statusBarsPadding()
            }.ifThen(navigationBarPadding) {
                navigationBarsPadding()
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
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
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
    EdModalBottomSheet(
        sheetState = sheetState,
        sheetContent = sheetContent,
        modifier = Modifier.ifThen(imePadding) {
            imePadding()
        },
        windowInsets = imeInsets.union(navigationBarInsets),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
                .ifThen(statusBarPadding) {
                    statusBarsPadding()
                }.ifThen(navigationBarPadding) {
                    navigationBarsPadding()
                },
        ) {
            content()
        }
    }
}
