package io.edugma.features.misc.settings

import io.edugma.features.misc.settings.appearance.SettingsAppearanceViewModel
import io.edugma.features.misc.settings.main.SettingsMainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val settingsFeaturesModule = module {
    factoryOf(::SettingsMainViewModel)
    factoryOf(::SettingsAppearanceViewModel)
}
