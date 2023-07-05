package io.edugma.core.designSystem.atoms.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
@NonRestartableComposable
fun EdLottie(
    lottiePainter: LottiePainter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    EdPlatformLottie(
        lottiePainter = lottiePainter,
        modifier = modifier,
        contentScale = contentScale,
    )
}
