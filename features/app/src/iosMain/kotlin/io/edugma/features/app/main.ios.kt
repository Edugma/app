package io.edugma.features.app

import com.moriatsushi.insetsx.WindowInsetsUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    WindowInsetsUIViewController {
        MainApp()
    }
