package com.edugma.core.designSystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import co.touchlab.kermit.Severity
import com.edugma.core.api.api.CrashAnalytics
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageRequestBuilder
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.toScale
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.seiko.imageloader.rememberImagePainter
import com.seiko.imageloader.rememberImageSuccessPainter
import com.seiko.imageloader.util.LogPriority
import com.seiko.imageloader.util.Logger
import io.ktor.http.decodeURLPart
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.charsets.forName
import io.ktor.utils.io.charsets.isSupported
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem

@Composable
fun rememberCachedIconPainter(
    model: String,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    return rememberAsyncImagePainter(
        model = model,
        imageLoader = LocalEdIconLoader.current,
        contentScale = contentScale,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
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
    placeholderPainter: (@Composable () -> Painter)? = null,
    errorPainter: (@Composable () -> Painter)? = null,
): Painter {
    val request = remember(model, contentScale, placeholderPainter, errorPainter) {
        ImageRequest {
            data(model)
            scale(contentScale.toScale())
        }
    }

    val action by rememberImageAction(request, imageLoader.loader)
    LaunchedEffect(action) {
        if (action is ImageAction.Failure) {
            val error = (action as ImageAction.Failure).error
            CrashAnalytics.logException(error)
        }

    }
    return rememberImageActionPainter(
        action = action,
        filterQuality = filterQuality,
        placeholderPainter = placeholderPainter,
        errorPainter = errorPainter,
    )
}

@Composable
fun AsyncImage(
    model: String?,
    imageLoader: BaseImageLoader = LocalEdImageLoader.current,
    contentScale: ContentScale = ContentScale.Fit,
    filterQuality: FilterQuality = DefaultFilterQuality,
    image: @Composable (Painter) -> Unit,
    placeholder: @Composable () -> Unit,
) {
    if (model == null) {
        placeholder()
    } else {
        val request = remember(model, contentScale) {
            ImageRequest {
                data(model)
                scale(contentScale.toScale())
            }
        }

        val action by rememberImageAction(request, imageLoader.loader)

        if (action is ImageAction.Failure || action is ImageAction.Loading) {
            placeholder()
        } else {
            val painter = rememberImageSuccessPainter(action as ImageAction.Success, filterQuality)
            image(painter)
        }
    }
}

// @Composable
// fun rememberAsyncImagePainter(
//    model: Int,
//    imageLoader: BaseImageLoader = LocalEdImageLoader.current,
//    contentScale: ContentScale = ContentScale.Fit,
//    filterQuality: FilterQuality = DefaultFilterQuality,
//    placeholderPainter: (@Composable () -> Painter)? = null,
//    errorPainter: (@Composable () -> Painter)? = null,
// ): Painter {
//    return rememberLibPainter2(
//        resId = model,
//        imageLoader = imageLoader.loader,
//        contentScale = contentScale,
//        filterQuality = filterQuality,
//        placeholderPainter = placeholderPainter,
//        errorPainter = errorPainter,
//    )
// }
//
// @Composable
// fun rememberAsyncImagePainter(
//    model: ImageRequest,
//    imageLoader: BaseImageLoader = LocalEdImageLoader.current,
//    contentScale: ContentScale = ContentScale.Fit,
//    filterQuality: FilterQuality = DefaultFilterQuality,
// ): Painter {
//    return rememberLibPainter2(
//        request = model,
//        imageLoader = imageLoader.loader,
//        contentScale = contentScale,
//        filterQuality = filterQuality,
//    )
// }

expect class IconImageLoader : BaseImageLoader

expect open class CommonImageLoader : BaseImageLoader

abstract class BaseImageLoader {
    internal lateinit var loader: ImageLoader

    internal fun init(
        memCacheSize: Int = 32 * 1024 * 1024, // 32MB
        diskCache: DiskCache? = null,
        isJs: Boolean = false,
        componentSetup: ComponentRegistryBuilder.() -> Unit,
    ) {

        this.loader = ImageLoader {
            interceptor {

                addInterceptor(
                    Interceptor {
                        if (it.request.data is String) {
                            val newRequest = ImageRequest {
                                takeFrom(it.request)
                                val url = it.request.data as String
                                // TODO
                                data(url.decodeURLPart(charset = Charsets.ISO_8859_1))
                            }
                            it.proceed(newRequest)
                        } else {
                            it.proceed(it.request)
                        }
                    }
                )
                bitmapMemoryCacheConfig {
                    maxSize(memCacheSize)
                }
                if (diskCache == null) {
                    diskCacheConfig(FakeFileSystem().apply { emulateUnix() }) {
                        directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
                        maxSizeBytes(256L * 1024 * 1024) // 256MB
                    }
                } else {
                    diskCacheConfig {
                        directory(diskCache.path.toPath())
                        maxSizeBytes(diskCache.size.toLong())
                    }
                }
                diskCache?.let {
                    diskCacheConfig {
                        directory(diskCache.path.toPath())
                        maxSizeBytes(diskCache.size.toLong())
                    }
                }
            }
            components {
                componentSetup()
            }
            logger = object : Logger {
                override fun isLoggable(priority: LogPriority): Boolean {
                    return true
                }

                override fun log(
                    priority: LogPriority,
                    tag: String,
                    data: Any?,
                    throwable: Throwable?,
                    message: String,
                ) {
                    val severity = when (priority) {
                        LogPriority.VERBOSE -> Severity.Verbose
                        LogPriority.DEBUG -> Severity.Debug
                        LogPriority.INFO -> Severity.Info
                        LogPriority.WARN -> Severity.Warn
                        LogPriority.ERROR -> Severity.Error
                        LogPriority.ASSERT -> Severity.Assert
                    }
                    co.touchlab.kermit.Logger.log(
                        severity = severity,
                        tag = tag,
                        throwable = throwable,
                        message = buildString {
                            if (data != null) {
                                append("[image data]")
                                append(data.toString().take(100))
                                append("\n")
                            }
                            append("[message] ")
                            append(message)
                        }
                    )
                }

            }
        }
    }
}

internal class DiskCache(
    val path: String,
    val size: Int = 512 * 1024 * 1024, // 512MB,
)
