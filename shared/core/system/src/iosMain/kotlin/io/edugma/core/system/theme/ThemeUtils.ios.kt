package io.edugma.core.system.theme

import io.edugma.core.api.model.ThemeMode
import platform.UIKit.UIApplication
import platform.UIKit.UIUserInterfaceStyle

actual object ThemeUtils {
    actual fun setTheme(mode: ThemeMode) {
        when (mode) {
            ThemeMode.Light -> {
                UIApplication.sharedApplication.keyWindow?.overrideUserInterfaceStyle =
                    UIUserInterfaceStyle.UIUserInterfaceStyleLight
            }
            ThemeMode.Dark -> {
                UIApplication.sharedApplication.keyWindow?.overrideUserInterfaceStyle =
                    UIUserInterfaceStyle.UIUserInterfaceStyleDark
            }
            ThemeMode.System -> {
                UIApplication.sharedApplication.keyWindow?.overrideUserInterfaceStyle =
                    UIUserInterfaceStyle.UIUserInterfaceStyleUnspecified
            }
        }
    }
}
