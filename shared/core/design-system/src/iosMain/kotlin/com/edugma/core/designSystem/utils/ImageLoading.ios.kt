package com.edugma.core.designSystem.utils

import coil3.PlatformContext
import com.edugma.core.api.repository.PathRepository

actual class IconImageLoader(
    pathRepository: PathRepository,
) : BaseImageLoader() {
    init {
        this.init(
            diskCacheConfig = {
                DiskCacheConfig(path = pathRepository.getIconCachePath())
            },
            context = PlatformContext.INSTANCE,
        ) {
        }
    }
}

actual open class CommonImageLoader(
    pathRepository: PathRepository,
) : BaseImageLoader() {
    init {
        this.init(
            diskCacheConfig = {
                DiskCacheConfig(path = pathRepository.getImageCachePath())
            },
            context = PlatformContext.INSTANCE,
        ) {
        }
    }
}
