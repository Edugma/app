package io.edugma.features.app

import androidx.compose.ui.window.ComposeUIViewController
import com.moriatsushi.insetsx.WindowInsetsUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        WindowInsetsUIViewController {
            MainApp()
        }
    }
