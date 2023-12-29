import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.instancekeeper.InstanceKeeperOwner
import io.edugma.features.app.core.initKoin
import io.edugma.features.app.presentation.main.MainScreen
import io.edugma.navigation.core.instanceKeeper.LocalInstanceKeeperOwner
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    onWasmReady {
        initKoin()
        CanvasBasedWindow("Edugma") {
            val mainInstanceKeeperOwner = remember {
                MainInstanceKeeperOwner()
            }

            CompositionLocalProvider(
                LocalInstanceKeeperOwner provides mainInstanceKeeperOwner,
            ) {
                MainScreen()
            }
        }
    }
}

class MainInstanceKeeperOwner : InstanceKeeperOwner {
    override val instanceKeeper: InstanceKeeper = InstanceKeeperDispatcher()
}
