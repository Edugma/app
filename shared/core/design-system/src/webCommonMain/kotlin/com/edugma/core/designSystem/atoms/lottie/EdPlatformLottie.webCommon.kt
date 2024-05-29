package com.edugma.core.designSystem.atoms.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
internal actual fun EdPlatformLottie(
    lottieSource: LottieSource,
    modifier: Modifier,
    contentScale: ContentScale,
) {
    EdKottie(
        lottieSource = lottieSource,
        modifier = modifier,
        contentScale = contentScale,
    )
}
