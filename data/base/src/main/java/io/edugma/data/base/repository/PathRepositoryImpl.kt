package io.edugma.data.base.repository

import android.content.Context
import io.edugma.domain.base.repository.PathRepository

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

// //iOS
// actual fun ComponentRegistryBuilder.setupDefaultComponents() = this.setupDefaultComponents()
// actual fun getImageCacheDirectoryPath(): Path {
//    val cacheDir = NSSearchPathForDirectoriesInDomains(
//        NSCachesDirectory,
//        NSUserDomainMask,
//        true
//    ).first() as String
//    return (cacheDir + "/media").toPath()
// }
}
