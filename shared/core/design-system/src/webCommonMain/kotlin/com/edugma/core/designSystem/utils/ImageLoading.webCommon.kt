package com.edugma.core.designSystem.utils

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
