package io.edugma.android.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            EdTheme(
                useDynamicColors = true,
            ) {
                EdSurface(
                    color = EdTheme.colorScheme.background,
                    elevation = EdElevation.Level0,
                ) {
                    MainContent()
                }
            }
        }
    }
}
