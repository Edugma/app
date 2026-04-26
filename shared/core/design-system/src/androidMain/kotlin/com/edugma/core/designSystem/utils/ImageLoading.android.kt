package com.edugma.core.designSystem.utils

import android.content.Context
import com.edugma.core.api.repository.PathRepository

actual class IconImageLoader(
    pathRepository: PathRepository,
    appContext: Context,
) : BaseImageLoader() {
    init {
        this.init(
            diskCacheConfig = {
                DiskCacheConfig(path = pathRepository.getIconCachePath())
            },
            context = appContext,
        ) {
        }
    }
}

actual open class CommonImageLoader(
    pathRepository: PathRepository,
    appContext: Context,
) : BaseImageLoader() {
    init {
        this.init(
            diskCacheConfig = {
                DiskCacheConfig(path = pathRepository.getImageCachePath())
            },
            context = appContext,
        ) {
        }
    }
}
