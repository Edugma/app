package io.edugma.core.designSystem.atoms.lottie

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@Composable
internal expect fun EdPlatformLottie(
    lottiePainter: LottiePainter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
)
