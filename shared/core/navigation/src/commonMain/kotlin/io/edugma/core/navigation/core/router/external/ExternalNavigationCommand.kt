package io.edugma.core.navigation.core.router.external

sealed interface ExternalNavigationCommand {
    data class Share(val text: String) : ExternalNavigationCommand
    data class OpenUri(val uri: String) : ExternalNavigationCommand
    data class Message(val text: String) : ExternalNavigationCommand
    data class OpenStore(val packageName: String?) : ExternalNavigationCommand
}
