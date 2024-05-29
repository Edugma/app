package com.edugma.features.misc.menu

import com.edugma.core.arch.mvi.viewmodel.BaseViewModel
import com.edugma.core.navigation.misc.SettingsScreens
import com.edugma.core.navigation.nodes.NodesScreens

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
