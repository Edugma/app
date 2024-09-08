package com.edugma.core.designSystem.utils

import android.content.Context
import com.edugma.core.api.repository.PathRepository
import com.seiko.imageloader.component.setupDefaultComponents

actual class IconImageLoader(
    pathRepository: PathRepository,
    appContext: Context,
) : BaseImageLoader() {
    init {
        this.init(
            diskCache = DiskCache(path = pathRepository.getIconCachePath()),
            componentSetup = {
                // Android
                setupDefaultComponents(appContext)
                // iOS
                // this.setupDefaultComponents()
            },
        )
    }
}

actual open class CommonImageLoader(
    pathRepository: PathRepository,
    appContext: Context,
) : BaseImageLoader() {
    init {
        this.init(
            // diskCache = DiskCache(path = pathRepository.getImageCachePath()),
            componentSetup = {
                // Android
                setupDefaultComponents(appContext)
                // iOS
                // this.setupDefaultComponents()
            },
        )
    }
}
