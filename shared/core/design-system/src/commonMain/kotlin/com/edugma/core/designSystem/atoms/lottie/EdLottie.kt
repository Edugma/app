package com.edugma.core.designSystem.atoms.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import edugma.shared.core.resources.generated.resources.Res
import com.edugma.core.api.utils.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
@NonRestartableComposable
fun EdLottie(
    lottieSource: LottieSource,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {

    var lottieSourceState by remember {
        mutableStateOf<LottieSource?>(null)
    }
    LaunchedEffect(Unit) {
        when (val source = lottieSource) {
            is LottieSource.FileRes -> {
                withContext(Dispatchers.IO) {
                    val jsonString = Res.readBytes(source.file).decodeToString()
                    lottieSourceState = LottieSource.JsonString(jsonString)
                }
            }
            is LottieSource.JsonString -> {
                lottieSourceState = LottieSource.JsonString(source.jsonString)
            }
            is LottieSource.Url -> {
                lottieSourceState = LottieSource.Url(source.url)
            }
        }
    }

    lottieSourceState?.let { lottieSourceState ->
        EdPlatformLottie(
            lottieSource = lottieSourceState,
            modifier = modifier,
            contentScale = contentScale,
        )
    }
}
