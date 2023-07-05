package io.edugma.core.designSystem.atoms.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter

@Composable
internal actual fun EdPlatformLottie(
    lottiePainter: LottiePainter,
    modifier: Modifier,
    contentScale: ContentScale,
) {
    val painter = rememberAsyncImagePainter(model = lottiePainter.alternativeUrl)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
    )
}
