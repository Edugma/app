package com.edugma.core.system.repository

import com.edugma.core.api.model.ThemeMode
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.api.repository.ThemeRepository
import com.edugma.core.system.theme.ThemeUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ThemeRepositoryImpl(
    private val settingsRepository: SettingsRepository,
) : ThemeRepository {

    override suspend fun init() {
        val mode = settingsRepository.getInt(SETTINGS_THEME_KEY)?.toTheme() ?: ThemeMode.System
        withContext(Dispatchers.Main) {
            ThemeUtils.setTheme(mode)
        }
    }
    override suspend fun setTheme(mode: ThemeMode) {
        withContext(Dispatchers.Main) {
            ThemeUtils.setTheme(mode)
        }
        settingsRepository.saveInt(SETTINGS_THEME_KEY, mode.toInt())
    }

    override fun getTheme(): Flow<ThemeMode> {
        return settingsRepository.getIntFlow(SETTINGS_THEME_KEY)
            .map { it?.toTheme() ?: ThemeMode.System }
    }

    private fun ThemeMode.toInt(): Int {
        return when (this) {
            ThemeMode.System -> 0
            ThemeMode.Light -> 1
            ThemeMode.Dark -> 2
        }
    }

    private fun Int.toTheme(): ThemeMode {
        return when (this) {
            1 -> ThemeMode.Light
            2 -> ThemeMode.Dark
            else -> ThemeMode.System
        }
    }

    companion object {
        private const val SETTINGS_THEME_KEY = "theme"
    }
}
