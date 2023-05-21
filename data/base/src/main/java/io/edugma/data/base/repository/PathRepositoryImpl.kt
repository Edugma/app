package io.edugma.data.base.repository

import android.content.Context
import io.edugma.domain.base.repository.PathRepository

class PathRepositoryImpl(
    private val appContext: Context,
) : PathRepository {
    override fun getDatastorePath(dataStoreFileName: String): String {
        return appContext.filesDir.resolve(dataStoreFileName).absolutePath
    }
}
