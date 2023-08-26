package io.edugma.core.system.theme

import androidx.appcompat.app.AppCompatDelegate
import io.edugma.core.api.model.ThemeMode

actual object ThemeUtils {
    actual fun setTheme(mode: ThemeMode) {
        when (mode) {
            ThemeMode.Light -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
            ThemeMode.Dark -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
            ThemeMode.System -> AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }
}
