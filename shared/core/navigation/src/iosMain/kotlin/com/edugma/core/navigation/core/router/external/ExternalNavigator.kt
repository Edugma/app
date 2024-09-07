package com.edugma.core.navigation.core.router.external

import co.touchlab.kermit.Logger
import com.edugma.core.api.api.CrashAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

class ExternalNavigator(
    private val externalRouter: ExternalRouter,
    // https://github.com/InsertKoinIO/koin/issues/1492
    private val contextHolder: UIViewControllerKoinHolder,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private val context: UIViewController = contextHolder.data

    fun start() {
        scope.launch {
            externalRouter.messageFlow.collect {
                try {
                    processCommand(it)
                } catch (e: Exception) {
                    CrashAnalytics.logException(e)
                }
            }
        }
    }

    fun onClose() {
        scope.cancel()
    }

    private fun processCommand(command: ExternalNavigationCommand) {
        when (command) {
            is ExternalNavigationCommand.Share -> share(command)
            is ExternalNavigationCommand.OpenUri -> openUri(command)
            is ExternalNavigationCommand.Message -> {
                // TODO not supported
            }
            is ExternalNavigationCommand.OpenStore -> openStore(command)
        }
    }

    private fun share(command: ExternalNavigationCommand.Share) {
        val textToShare = listOf(command.text)
        val activityViewController = UIActivityViewController(
            activityItems = textToShare,
            applicationActivities = null,
        )
        context.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null,
        )
       // activityViewController.popoverPresentationController?.sourceView = self.view // so that iPads won't crash
    }

    private fun openUri(command: ExternalNavigationCommand.OpenUri) {
        val url = NSURL.URLWithString(command.uri) ?: return
        UIApplication.sharedApplication.openURL(
            url = url,
            options = emptyMap<Any?, Any>(),
            completionHandler = null,
        )
    }

    private fun openStore(command: ExternalNavigationCommand.OpenStore) {
        val id = command.appPage.appStoreId

        val url = NSURL.URLWithString("itms-apps://itunes.apple.com/app/id$id") ?: return
        if (UIApplication.sharedApplication.canOpenURL(url).not()) return
        UIApplication.sharedApplication.openURL(
            url = url,
            options = emptyMap<Any?, Any>(),
            completionHandler = null,
        )

    }
}
