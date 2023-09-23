package io.edugma.features.app.data

import io.edugma.core.api.repository.BuildConfigRepository
import io.edugma.shared.app.BuildKonfig

class BuildConfigRepositoryImpl : BuildConfigRepository {
    override fun getBuildType(): String {
        return BuildKonfig.BuildType
    }

    override fun getVersion(): String {
        return BuildKonfig.Version
    }

    override fun getPlatform(): String {
        return BuildKonfig.Platform
    }

    override fun isLogsEnabled(): Boolean {
        return BuildKonfig.IsLogsEnabled
    }

    override fun isNetworkLogsEnabled(): Boolean {
        return BuildKonfig.IsNetworkLogsEnabled
    }

    override fun isDebugPanelEnabled(): Boolean {
        return BuildKonfig.IsDebugPanelEnabled
    }
}
