package com.edugma.features.misc.menu

import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.navigation.misc.MiscMenuScreens
import com.edugma.core.navigation.misc.SettingsScreens
import com.edugma.core.navigation.nodes.NodesScreens

class MiscMenuViewModel : FeatureLogic<Unit, MiscMenuAction>() {
    override fun initialState() {
        return Unit
    }

    override fun processAction(action: MiscMenuAction) {
        when (action) {
            MiscMenuAction.AboutAppClick -> miscRouter.navigateTo(MiscMenuScreens.AboutApp())
            MiscMenuAction.NodesClick -> tabMenuRouter.navigateTo(NodesScreens.Main())
            MiscMenuAction.SettingsClick -> miscRouter.navigateTo(SettingsScreens.Main())
        }
    }
}
