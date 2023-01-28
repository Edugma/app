package io.edugma.android.features

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.IconCacheLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IconCacheLoader.initLoader(this)

        installSplashScreen().apply {
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            EdTheme(
                useDynamicColors = true,
            ) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Box(
                        Modifier,
                    ) {
                        MainContent()
                    }
                }
            }
        }
    }
}
