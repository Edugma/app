package io.edugma.core.navigation.core.router.external

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
            externalRouter.commandBuffer.collect {
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

    private fun processCommand(commands: List<ExternalNavigationCommand>) {
        for (command in commands) {
            applyCommand(command)
        }
    }

    private fun applyCommand(command: ExternalNavigationCommand) {
        when (command) {
            is ExternalNavigationCommand.Share -> share(command)
            is ExternalNavigationCommand.OpenUri -> openUri(command)
            is ExternalNavigationCommand.Message -> showMessage(command)
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
}
