package com.edugma.features.misc.other.inAppUpdate.domain

data class SemVer(
    val major: Int,
    val minor: Int,
    val patch: Int,
    val stage: VersionStage,
) {
    operator fun compareTo(other: SemVer): Int {
        val majorCmp = this.major.compareTo(other.major)
        if (majorCmp != 0) return majorCmp

        val minorCmp = this.minor.compareTo(other.minor)
        if (minorCmp != 0) return minorCmp

        val patchCmp = this.patch.compareTo(other.patch)
        if (patchCmp != 0) return patchCmp

        return this.stage.order.compareTo(other.stage.order)
    }
}
