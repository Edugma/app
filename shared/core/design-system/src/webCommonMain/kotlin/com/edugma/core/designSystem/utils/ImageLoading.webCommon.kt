package com.edugma.core.designSystem.utils

import com.seiko.imageloader.component.setupDefaultComponents

actual class IconImageLoader() : BaseImageLoader() {
    init {
        this.init(
            componentSetup = {
                this.setupDefaultComponents()
            },
        )
    }
}

actual open class CommonImageLoader() : BaseImageLoader() {
    init {
        this.init(
            componentSetup = {
                this.setupDefaultComponents()
            },
        )
    }
}
