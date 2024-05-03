import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import io.edugma.features.app.core.initKoin
import io.edugma.features.app.presentation.main.MainScreen
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        initKoin()
        CanvasBasedWindow("Edugma") {

            CompositionLocalProvider(
            ) {
                MainScreen()
            }
        }
    }
}
