package io.edugma.core.system.theme

import io.edugma.core.api.model.ThemeMode

actual object ThemeUtils {
    actual fun setTheme(mode: ThemeMode) {
        when (mode) {
            ThemeMode.Light -> {
                UIApplication.shared.keyWindow.overrideUserInterfaceStyle =
                    UIUserInterfaceStyle.light
            }
            ThemeMode.Dark -> {
                UIApplication.shared.keyWindow.overrideUserInterfaceStyle =
                    UIUserInterfaceStyle.dark
            }
            ThemeMode.System -> {
                UIApplication.shared.keyWindow.overrideUserInterfaceStyle =
                    UIUserInterfaceStyle.unspecified
            }
        }
    }
}
