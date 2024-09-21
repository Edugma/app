package com.edugma.features.misc.menu

import com.edugma.core.arch.mvi.viewmodel.FeatureLogic2
import com.edugma.core.navigation.misc.SettingsScreens
import com.edugma.core.navigation.nodes.NodesScreens

class MiscMenuViewModel : FeatureLogic2<Unit>() {
    override fun initialState() {
        return Unit
    }
    fun onSettingsClick() {
        miscRouter.navigateTo(
            SettingsScreens.Main(),
        )
    }

    fun onNodeClick() {
        tabMenuRouter.navigateTo(NodesScreens.Main())
    }
}
