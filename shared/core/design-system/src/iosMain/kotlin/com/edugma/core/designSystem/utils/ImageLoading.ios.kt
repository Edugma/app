package com.edugma.core.designSystem.utils

import com.edugma.core.api.repository.PathRepository
import com.seiko.imageloader.component.setupDefaultComponents

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
