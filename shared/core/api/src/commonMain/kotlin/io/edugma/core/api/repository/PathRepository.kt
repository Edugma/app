package io.edugma.core.api.repository

interface PathRepository {
    fun getDatastorePath(dataStoreFileName: String): String

    fun getImageCachePath(): String
    fun getIconCachePath(): String
}
