package com.edugma.features.misc.aboutApp

sealed interface AboutAppAction {
    data object OnBack : AboutAppAction
    data object TelegramClick : AboutAppAction
    data object VkClick : AboutAppAction
    data object SourceCodeClick : AboutAppAction
}
