package io.edugma.core.api.repository

import io.edugma.core.api.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    suspend fun init()
    suspend fun setTheme(mode: ThemeMode)
    fun getTheme(): Flow<ThemeMode>
}
