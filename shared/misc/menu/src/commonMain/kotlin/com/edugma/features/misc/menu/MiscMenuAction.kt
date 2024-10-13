package com.edugma.features.misc.menu

sealed interface MiscMenuAction {
    data object SettingsClick : MiscMenuAction
    data object NodesClick : MiscMenuAction
    data object AboutAppClick : MiscMenuAction
}
