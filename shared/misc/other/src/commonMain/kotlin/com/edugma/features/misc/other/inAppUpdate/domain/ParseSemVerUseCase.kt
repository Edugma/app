package com.edugma.features.misc.other.inAppUpdate.domain

class ParseSemVerUseCase {
    operator fun invoke(versionText: String): SemVer {
        val versions = versionText.split('.', '-')

        return SemVer(
            major = versions[0].toInt(),
            minor = versions[1].toInt(),
            patch = versions[2].toInt(),
            stage = versions.getOrNull(3).toVersionStage(),
        )
    }

    private fun String?.toVersionStage(): VersionStage {
        return when (this) {
            null -> VersionStage.Release
            "beta" -> VersionStage.Beta
            "alpha" -> VersionStage.Alpha
            else -> VersionStage.Unknown
        }
    }
}
