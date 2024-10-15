package com.edugma.core.api.repository

interface CleanupRepository {
    suspend fun cleanAll()
}
