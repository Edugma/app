import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.edugma.core.api.utils.JsJodaTimeZoneModule
import com.edugma.features.app.core.initKoin
import com.edugma.features.app.presentation.main.MainScreen
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val jsJodaTz = JsJodaTimeZoneModule
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
