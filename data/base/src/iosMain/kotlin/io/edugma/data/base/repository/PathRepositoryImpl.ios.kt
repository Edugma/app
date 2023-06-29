package io.edugma.data.base.repository

import io.edugma.domain.base.repository.PathRepository
import platform.Foundation.FileSystem.FileManager.NSSearchPathForDirectoriesInDomains
import platform.Foundation.FileSystem.NSFileManager.NSSearchPathDirectory.NSCachesDirectory
import platform.Foundation.FileSystem.NSFileManager.NSSearchPathDomainMask.NSUserDomainMask

class PathRepositoryImpl() : PathRepository {
    override fun getDatastorePath(dataStoreFileName: String): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        val path = requireNotNull(documentDirectory).path

        return "$path/$dataStoreFileName"
    }

    override fun getImageCachePath(): String {
        val cacheDir = getCacheDir()
        return "$cacheDir/images"
    }

    override fun getIconCachePath(): String {
        val cacheDir = getCacheDir()
        return "$cacheDir/icons"
    }

    private fun getCacheDir(): String {
        return NSSearchPathForDirectoriesInDomains(
            NSCachesDirectory,
            NSUserDomainMask,
            true,
        ).first() as String
    }
}
