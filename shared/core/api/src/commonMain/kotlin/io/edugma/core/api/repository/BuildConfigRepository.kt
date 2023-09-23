package io.edugma.core.api.repository

interface BuildConfigRepository {
    fun getBuildType(): String
    fun getVersion(): String
    fun getPlatform(): String

    fun isLogsEnabled(): Boolean
    fun isNetworkLogsEnabled(): Boolean
    fun isDebugPanelEnabled(): Boolean
}
