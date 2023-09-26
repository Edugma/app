package io.edugma.core.navigation.core.router.external

import io.edugma.core.api.repository.BaseCommandBus

class ExternalRouter : BaseCommandBus<ExternalNavigationCommand>() {
    fun share(text: String) {
        sendCommand(
            ExternalNavigationCommand.Share(
                text = text,
            ),
        )
    }

    fun openUri(uri: String) {
        sendCommand(
            ExternalNavigationCommand.OpenUri(
                uri = uri,
            ),
        )
    }

    fun showMessage(text: String) {
        sendCommand(
            ExternalNavigationCommand.Message(
                text = text,
            ),
        )
    }

    /**
     * @param packageName Package of app to open. Will open current app page in store if equal null.
     */
    fun openStore(packageName: String? = null) {
        sendCommand(
            ExternalNavigationCommand.OpenStore(
                packageName = packageName,
            ),
        )
    }
}
