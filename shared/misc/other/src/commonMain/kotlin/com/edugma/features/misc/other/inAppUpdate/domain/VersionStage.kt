package com.edugma.features.misc.other.inAppUpdate.domain

enum class VersionStage(val order: Int) {
    Unknown(-1),
    Alpha(0),
    Beta(1),
    Release(Int.MAX_VALUE),
}
