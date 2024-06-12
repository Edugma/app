package com.edugma.core.designSystem.atoms.lottie

import Lottie.CompatibleAnimationView
import Lottie.CompatibleRenderingEngineOptionAutomatic
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.layout.ContentScale
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIColor
import platform.UIKit.UIView
import platform.UIKit.UIViewContentMode

@OptIn(ExperimentalForeignApi::class)
@Composable
internal actual fun EdPlatformLottie(
    lottieSource: LottieSource,
    backgroundColor: Color,
    modifier: Modifier,
    contentScale: ContentScale,
) {
    val composition = rememberLottieComposition(lottieSource)

    if (composition != null) {
        LaunchedEffect(lottieSource) {
            composition.setLoopAnimationCount(Int.MAX_VALUE.toDouble())
            composition.play()
        }

        LottieAnimation(
            modifier = modifier,
            composition = composition,
            backgroundColor = backgroundColor,
            contentScale = contentScale,
        )
    }
}


@OptIn(ExperimentalForeignApi::class)
@Composable
fun LottieAnimation(
    modifier: Modifier,
    backgroundColor: Color,
    composition: CompatibleAnimationView,
    contentScale: ContentScale,
) {
    UIKitView(
        modifier = modifier.drawBehind {

        },
        factory = {
            UIView().apply {
                this.backgroundColor = UIColor.clearColor
                opaque = false
                clipsToBounds = true
            }
        },
        interactive = false,
        background = backgroundColor,
        update = {
            composition.translatesAutoresizingMaskIntoConstraints = false

            it.addSubview(composition)
            it.opaque = false

            NSLayoutConstraint.activateConstraints(
                listOf(
                    composition.widthAnchor.constraintEqualToAnchor(it.widthAnchor),
                    composition.heightAnchor.constraintEqualToAnchor(it.heightAnchor)
                )
            )

            val scale = contentScale.toIosContentScale()
            composition.setContentMode(scale)
        }
    )
}

private fun ContentScale.toIosContentScale(): UIViewContentMode {
    return when (this) {
        // TODO
        ContentScale.FillWidth -> UIViewContentMode.UIViewContentModeScaleToFill
        else -> UIViewContentMode.UIViewContentModeScaleAspectFit
    }
}



@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.toULong())
}


@OptIn(ExperimentalForeignApi::class)
@Composable
internal fun rememberLottieComposition(
    spec: LottieSource
): CompatibleAnimationView? {

    var animationState by remember(spec) {
        mutableStateOf<CompatibleAnimationView?>(null)
    }

    LaunchedEffect(spec) {
        when (spec) {
            is LottieSource.FileRes -> {
//                CompatibleAnimationView(
//                    data = spec.jsonString.encodeToByteArray().toNSData(),
//                    compatibleRenderingEngineOption = CompatibleRenderingEngineOptionAutomatic
//                )
            }

            is LottieSource.Url -> {
//                val httpClient = HttpClient()
//                val data = httpClient.get(spec.url)
//                CompatibleAnimationView(
//                    data = data.readBytes().toNSData(),
//                    compatibleRenderingEngineOption = CompatibleRenderingEngineOptionAutomatic
//                )
            }

            is LottieSource.JsonString -> {
                animationState = CompatibleAnimationView(
                    data = spec.jsonString.encodeToByteArray().toNSData(),
                    compatibleRenderingEngineOption = CompatibleRenderingEngineOptionAutomatic
                )
            }

        }
    }

    return animationState
}

