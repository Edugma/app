package io.edugma.core.system.theme

import io.edugma.core.api.model.ThemeMode

expect object ThemeUtils {
    fun setTheme(mode: ThemeMode)
}
