package com.edugma.features.app

import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.LifecycleStartEffect
import com.edugma.core.navigation.core.router.external.ExternalNavigator
import com.edugma.core.navigation.core.router.external.UIViewControllerKoinHolder
import com.edugma.features.app.presentation.main.MainScreen
import org.koin.compose.currentKoinScope
import org.koin.core.parameter.parametersOf
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController =
    ComposeUIViewController {
        MainScreen()

        val scope = currentKoinScope()

        LifecycleStartEffect(scope) {
            val window = UIApplication.sharedApplication.windows.firstOrNull() as? UIWindow
            val controller = window?.rootViewController
            if (controller == null) {
                onStopOrDispose { }
            } else {
                val controllerHolder = UIViewControllerKoinHolder(controller)
                val externalNavigator = scope.get<ExternalNavigator> { parametersOf(controllerHolder) }

                externalNavigator.start()

                onStopOrDispose {
                    externalNavigator.onClose()
                }
            }
        }
    }
