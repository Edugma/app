package io.edugma.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import io.edugma.core.navigation.core.router.external.ExternalNavigator
import io.edugma.features.app.MainApp
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {

    private var externalNavigator: ExternalNavigator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MainApp()
        }

        externalNavigator = inject<ExternalNavigator> { parametersOf(this) }.value

        externalNavigator?.start()
    }

    override fun onDestroy() {
        externalNavigator?.onClose()
        externalNavigator = null
        super.onDestroy()
    }
}
