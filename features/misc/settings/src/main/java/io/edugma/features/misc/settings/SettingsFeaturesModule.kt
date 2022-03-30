package io.edugma.features.misc.settings

import io.edugma.features.misc.settings.appearance.SettingsAppearanceViewModel
import io.edugma.features.misc.settings.main.SettingsMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsFeaturesModule = module {
    viewModel { SettingsMainViewModel() }
    viewModel { SettingsAppearanceViewModel() }
}