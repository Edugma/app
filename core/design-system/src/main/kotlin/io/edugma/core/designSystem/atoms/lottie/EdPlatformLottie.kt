package io.edugma.core.designSystem.atoms.lottie

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter

// android
@Composable
internal fun EdPlatformLottie(
    lottiePainter: LottiePainter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {

    val composition by rememberLottieComposition(spec = lottiePainter.source.toLottie())
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
        is LottieSource.Asset -> LottieCompositionSpec.Asset(this.assetName)
        is LottieSource.File -> LottieCompositionSpec.File(this.fileName)
        is LottieSource.JsonString -> LottieCompositionSpec.JsonString(this.jsonString)
        is LottieSource.RawRes -> LottieCompositionSpec.RawRes(this.resId)
        is LottieSource.Url -> LottieCompositionSpec.Url(this.url)
    }
}

// ios
@Composable
internal fun EdPlatformLottie2(
    lottiePainter: LottiePainter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val painter = rememberAsyncImagePainter(model = lottiePainter.alternativeUrl)

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
    )
}
