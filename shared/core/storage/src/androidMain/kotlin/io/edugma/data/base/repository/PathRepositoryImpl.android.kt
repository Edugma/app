package io.edugma.data.base.repository

import android.content.Context
import io.edugma.core.api.repository.PathRepository

class PathRepositoryImpl(
    private val appContext: Context,
) : PathRepository {
    override fun getDatastorePath(dataStoreFileName: String): String {
        return appContext.filesDir.resolve(dataStoreFileName).absolutePath
    }

    override fun getImageCachePath(): String {
        return appContext.cacheDir.resolve("images").absolutePath
    }

    override fun getIconCachePath(): String {
        return appContext.cacheDir.resolve("icons").absolutePath
    }
}
