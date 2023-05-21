package io.edugma.domain.base.repository

interface PathRepository {
    fun getDatastorePath(dataStoreFileName: String): String
}
