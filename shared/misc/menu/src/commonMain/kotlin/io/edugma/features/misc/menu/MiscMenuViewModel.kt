package io.edugma.features.misc.menu

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.navigation.misc.SettingsScreens
import io.edugma.core.navigation.nodes.NodesScreens

class MiscMenuViewModel : BaseViewModel<Unit>(Unit) {
    fun onSettingsClick() {
        miscRouter.navigateTo(
            SettingsScreens.Main(),
        )
    }

    fun onNodeClick() {
        tabMenuRouter.navigateTo(NodesScreens.Main())
    }
}
