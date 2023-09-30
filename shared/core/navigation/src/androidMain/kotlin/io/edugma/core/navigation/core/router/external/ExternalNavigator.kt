package io.edugma.core.navigation.core.router.external

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class ExternalNavigator(
    private val externalRouter: ExternalRouter,
    private val context: Context,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun start() {
        scope.launch {
            externalRouter.messageFlow.collect {
                try {
                    processCommand(it)
                } catch (e: Exception) {
                    Logger.e("", e)
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
            is ExternalNavigationCommand.Message -> showMessage(command)
            is ExternalNavigationCommand.OpenStore -> openStore(command)
        }
    }

    private fun share(command: ExternalNavigationCommand.Share) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, command.text)
            type = "text/plain"
        }
            .let { Intent.createChooser(it, null) }

        context.startActivity(intent)
    }

    private fun openUri(command: ExternalNavigationCommand.OpenUri) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(command.uri)
        }

        context.startActivity(intent)
    }

    private fun showMessage(command: ExternalNavigationCommand.Message) {
        Toast.makeText(context, command.text, Toast.LENGTH_SHORT).show()
    }

    private fun openStore(command: ExternalNavigationCommand.OpenStore) {
        val packageName = command.packageName ?: context.packageName
        try {
            context.startActivity(
                Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse("market://details?id=$packageName")
                    addGooglePlayComponent()
                },
            )
        } catch (e: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse("market://details?id=$packageName")
                    },
                )
            } catch (e: ActivityNotFoundException) {
                context.startActivity(
                    Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    },
                )
            }
        }
    }

    private fun Intent.addGooglePlayComponent() {
        // TODO Replace with installerPackage
        val otherApps = context.packageManager.queryIntentActivities(this, 0)
        for (otherApp in otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {
                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(
                    otherAppActivity.applicationInfo.packageName,
                    otherAppActivity.name,
                )

                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                component = componentName
            }
        }
    }
}
