package com.edugma.core.system.theme

import com.edugma.core.api.model.ThemeMode

expect object ThemeUtils {
    fun setTheme(mode: ThemeMode)
}
