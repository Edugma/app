package com.edugma.android

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.edugma.core.api.model.ThemeMode
import com.edugma.core.api.repository.ThemeRepository
import com.edugma.core.navigation.core.router.external.ExternalNavigator
import com.edugma.features.app.presentation.main.MainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    private var externalNavigator: ExternalNavigator? = null
    private var themeRepository: ThemeRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
        }
        // including IME animations, and go edge-to-edge
        // This also sets up the initial system bar style based on the platform theme
        enableEdgeToEdge()

        themeRepository = inject<ThemeRepository>().value
        val isDarkTheme = mutableStateOf(isSystemInDarkTheme())

        lifecycleScope.launch(Dispatchers.IO) {
            themeRepository?.getTheme()?.collect {
                when (it) {
                    ThemeMode.Light -> isDarkTheme.value = false
                    ThemeMode.Dark -> isDarkTheme.value = true
                    ThemeMode.System -> isDarkTheme.value = isSystemInDarkTheme()
                }
            }
        }


        setContent {
            val darkTheme = isDarkTheme.value
            // Update the edge to edge configuration to match the theme
            // This is the same parameters as the default enableEdgeToEdge call, but we manually
            // resolve whether or not to show dark theme using uiState, since it can be different
            // than the configuration's dark theme value based on the user preference.
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim,
                        darkScrim,
                    ) { darkTheme },
                )
                onDispose {  }
            }
            ProvideActivityViewModelStoreOwner {
                MainScreen()
            }
        }

        externalNavigator = inject<ExternalNavigator> { parametersOf(this) }.value

        externalNavigator?.start()
    }

    private fun isSystemInDarkTheme(): Boolean {
        val uiMode = Configuration(resources.configuration).uiMode
        return (uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onDestroy() {
        externalNavigator?.onClose()
        externalNavigator = null
        super.onDestroy()
    }

        /**
         * The default light scrim, as defined by androidx and the platform:
         * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
         */
        private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

        /**
         * The default dark scrim, as defined by androidx and the platform:
         * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
         */
        private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
}
