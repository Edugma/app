package com.edugma.core.designSystem.atoms.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

// android
@Composable
internal actual fun EdPlatformLottie(
    lottieSource: LottieSource,
    modifier: Modifier,
    contentScale: ContentScale,
) {

    val composition by rememberLottieComposition(spec = lottieSource.toLottie())
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress },
        contentScale = contentScale,
    )
}

private fun LottieSource.toLottie(): LottieCompositionSpec {
    return when (this) {
        is LottieSource.FileRes -> LottieCompositionSpec.File(this.file)
        is LottieSource.JsonString -> LottieCompositionSpec.JsonString(this.jsonString)
        is LottieSource.Url -> LottieCompositionSpec.Url(this.url)
    }
}
