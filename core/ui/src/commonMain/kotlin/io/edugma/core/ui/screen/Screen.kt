package io.edugma.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moriatsushi.insetsx.navigationBarsPadding
import com.moriatsushi.insetsx.statusBarsPadding
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
                statusBarsPadding()
            }.ifThen(navigationBarPadding) {
                navigationBarsPadding()
            },
    ) {
        content()
    }
}
