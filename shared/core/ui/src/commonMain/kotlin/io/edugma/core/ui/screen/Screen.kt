package io.edugma.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.utils.ifThen
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
