package com.edugma.core.api.utils

enum class EdugmaPlatform {
    Android,
    Ios,
    Web
}

expect val currentPlatform: EdugmaPlatform
