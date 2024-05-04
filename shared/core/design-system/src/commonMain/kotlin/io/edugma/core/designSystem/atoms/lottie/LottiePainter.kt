package io.edugma.core.designSystem.atoms.lottie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import kotlin.jvm.JvmInline

@Immutable
sealed interface LottieSource {
    /**
     * Load an animation from the internet. Lottie has a default network stack that will use
     * standard Android networking APIs to attempt to load your animation. You may want to
     * integrate your own networking stack instead for consistency, to add your own headers,
     * or implement retries. To do that, call [com.airbnb.lottie.Lottie.initialize] and set
     * a [com.airbnb.lottie.network.LottieNetworkFetcher] on the [com.airbnb.lottie.LottieConfig].
     *
     * If you are using this spec, you may want to use [rememberLottieComposition] instead of
     * passing this spec directly into [LottieAnimation] because it can fail and you want to
     * make sure that you properly handle the failures and/or retries.
     */
    @JvmInline
    value class Url(val url: String) : LottieSource

    /**
     * Load an animation from an arbitrary file. Make sure that your app has permissions to read it
     * or else this may fail.
     */
    @JvmInline
    value class FileRes(val file: String) : LottieSource

    /**
     * Load an animation from its json string.
     */
    @JvmInline
    value class JsonString(val jsonString: String) : LottieSource
}
