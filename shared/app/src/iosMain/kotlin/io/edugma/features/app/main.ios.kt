package io.edugma.features.app

import androidx.compose.ui.window.ComposeUIViewController
import io.edugma.features.app.presentation.main.MainScreen
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        MainScreen()
    }
