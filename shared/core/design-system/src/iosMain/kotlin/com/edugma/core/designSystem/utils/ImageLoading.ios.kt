package com.edugma.core.designSystem.utils

import com.seiko.imageloader.component.setupDefaultComponents
import com.edugma.core.api.repository.PathRepository

actual class IconImageLoader(
    pathRepository: PathRepository,
) : BaseImageLoader() {
    init {
        this.init(
            diskCache = DiskCache(path = pathRepository.getIconCachePath()),
            componentSetup = {
                this.setupDefaultComponents()
            },
        )
    }
}

actual open class CommonImageLoader(
    pathRepository: PathRepository,
) : BaseImageLoader() {
    init {
        this.init(
            diskCache = DiskCache(path = pathRepository.getImageCachePath()),
            componentSetup = {
                this.setupDefaultComponents()
            },
        )
    }
}
