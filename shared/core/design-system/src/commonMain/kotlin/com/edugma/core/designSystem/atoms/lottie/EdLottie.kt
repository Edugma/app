package com.edugma.core.designSystem.atoms.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import edugma.shared.core.resources.generated.resources.Res
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
@NonRestartableComposable
fun EdLottie(
    lottieSource: LottieSource,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val composition by rememberLottieComposition {
        when (lottieSource) {
            is LottieSource.FileRes -> {
                LottieCompositionSpec.JsonString(
                    Res.readBytes(lottieSource.file).decodeToString()
                )
            }
            is LottieSource.JsonString -> {
                LottieCompositionSpec.JsonString(lottieSource.jsonString)
            }
            is LottieSource.Url -> {
                LottieCompositionSpec.Url(lottieSource.url)
            }
        }
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever,
    )

    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress },
        ),
        modifier = modifier,
        contentScale = contentScale,
        contentDescription = "Lottie animation"
    )
}
