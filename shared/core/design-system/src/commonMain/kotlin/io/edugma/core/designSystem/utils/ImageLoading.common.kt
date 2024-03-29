package io.edugma.core.designSystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.toScale
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImagePainter
import com.seiko.imageloader.rememberImageSuccessPainter
import okio.Path.Companion.toPath

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
    return rememberImagePainter(
        request = remember(model, contentScale, placeholderPainter, errorPainter) {
            ImageRequest {
                data(model)
                scale(contentScale.toScale())
            }
        },
        imageLoader = imageLoader.loader,
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
                memoryCacheConfig {
                    maxSizeBytes(memCacheSize)
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
        }
    }
}

internal class DiskCache(
    val path: String,
    val size: Int = 512 * 1024 * 1024, // 512MB,
)
