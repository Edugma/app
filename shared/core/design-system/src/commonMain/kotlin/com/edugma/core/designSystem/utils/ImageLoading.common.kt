package com.edugma.core.designSystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Severity
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.request.ImageResult
import coil3.util.Logger
import com.edugma.core.api.api.CrashAnalytics
import io.ktor.http.decodeURLPart
import io.ktor.utils.io.charsets.Charsets
import okio.Path.Companion.toPath

@Composable
fun rememberCachedIconPainter(
    model: String,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    return rememberAsyncImagePainter(
        model = model,
        imageLoader = LocalEdIconLoader.current,
        contentScale = contentScale,
        filterQuality = filterQuality,
    )
}

val LocalEdImageLoader = staticCompositionLocalOf<BaseImageLoader> {
    error("CompositionLocal LocalEdImageLoader not present")
}
val LocalEdIconLoader = staticCompositionLocalOf<BaseImageLoader> {
    error("CompositionLocal LocalEdIconLoader not present")
}

@Composable
fun rememberAsyncImagePainter(
    model: String,
    imageLoader: BaseImageLoader = LocalEdImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
): Painter {
    return coil3.compose.rememberAsyncImagePainter(
        model = model,
        imageLoader = imageLoader.loader,
        contentScale = contentScale,
        onError = {
            CrashAnalytics.logException(it.result.throwable)
        },
        filterQuality = filterQuality,
    )
}

@Composable
fun AsyncImage(
    model: String?,
    modifier: Modifier = Modifier,
    imageLoader: BaseImageLoader = LocalEdImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholder: @Composable () -> Unit,
) {
    if (model == null) {
        placeholder()
    } else {
        coil3.compose.AsyncImage(
            model = model,
            contentDescription = null,
            modifier = modifier,
            contentScale = contentScale,
            filterQuality = filterQuality,
            imageLoader = imageLoader.loader,
        )
    }
}

expect class IconImageLoader : BaseImageLoader

expect open class CommonImageLoader : BaseImageLoader

abstract class BaseImageLoader {
    lateinit var loader: ImageLoader
        private set

    internal fun init(
        memCacheSize: Long = 32 * 1024 * 1024, // 32MB
        diskCacheConfig: (() -> DiskCacheConfig)? = null,
        context: PlatformContext,
        setup: ImageLoader.Builder.() -> Unit,
    ) {
        // TODO setup config in percents
        this.loader = ImageLoader.Builder(context)
            .components {
                add(FixIosUrlInterceptor())
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizeBytes(memCacheSize)
                    .build()
            }
            .apply {
                if (diskCacheConfig != null) {
                    diskCache {
                        val config = diskCacheConfig()
                        DiskCache.Builder()
                            .directory(config.path.toPath())
                            .maxSizeBytes(config.size)
                            .build()
                    }
                }
            }
            .logger(CoilLogger())
            .apply { setup() }.build()
    }
}

private class CoilLogger : Logger {

    override var minLevel: Logger.Level = Logger.Level.Info

    override fun log(
        tag: String,
        level: Logger.Level,
        message: String?,
        throwable: Throwable?,
    ) {
        val severity = when (level) {
            Logger.Level.Verbose -> Severity.Verbose
            Logger.Level.Debug -> Severity.Debug
            Logger.Level.Info -> Severity.Info
            Logger.Level.Warn -> Severity.Warn
            Logger.Level.Error -> Severity.Error
        }
        co.touchlab.kermit.Logger.log(
            severity = severity,
            tag = tag,
            throwable = throwable,
            message = message.orEmpty(),
        )
    }
}

private class FixIosUrlInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = chain.request
        return if (request.data is String) {
            val url = request.data as String
            // fix for iOS
            val newUrl = url.decodeURLPart(charset = Charsets.ISO_8859_1)
            val newRequest = request.newBuilder()
                .data(newUrl)
                .build()

            chain.withRequest(newRequest)
            chain.proceed()
        } else {
            chain.proceed()
        }
    }
}

internal class DiskCacheConfig(
    val path: String,
    val size: Long = 512 * 1024 * 1024, // 512MB,
)
