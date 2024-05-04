package io.edugma.core.designSystem.atoms.lottie

import KottieAnimation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

@Composable
@NonRestartableComposable
internal fun EdKottie(
    lottieSource: LottieSource,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val kottieSource = when (val source = lottieSource) {
        is LottieSource.FileRes -> KottieCompositionSpec.File(source.file)
        is LottieSource.JsonString -> KottieCompositionSpec.JsonString(source.jsonString)
        is LottieSource.Url -> KottieCompositionSpec.Url(source.url)
    }

    val composition = rememberKottieComposition(
        spec = kottieSource,
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        iterations = KottieConstants.IterateForever,
    )

    KottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { animationState.progress },
    )
}
