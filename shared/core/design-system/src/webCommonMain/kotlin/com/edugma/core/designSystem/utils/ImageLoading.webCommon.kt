package com.edugma.core.designSystem.utils

import com.seiko.imageloader.component.setupDefaultComponents
import com.edugma.core.api.repository.PathRepository

actual class IconImageLoader(
) : BaseImageLoader() {
    init {
        this.init(
            componentSetup = {
                this.setupDefaultComponents()
            },
        )
    }
}

actual open class CommonImageLoader(
) : BaseImageLoader() {
    init {
        this.init(
            componentSetup = {
                this.setupDefaultComponents()
            },
        )
    }
}
