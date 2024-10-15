package com.edugma.features.misc.aboutApp

import com.edugma.core.api.repository.BuildConfigRepository
import com.edugma.core.arch.mvi.viewmodel.FeatureLogic
import com.edugma.core.navigation.core.router.external.ExternalRouter

class AboutAppViewModel(
    private val buildConfigRepository: BuildConfigRepository,
    private val externalRouter: ExternalRouter,
) : FeatureLogic<AboutAppUiState, AboutAppAction>() {
    override fun initialState(): AboutAppUiState {
        return AboutAppUiState()
    }

    override fun onCreate() {
        newState {
            copy(
                version = buildConfigRepository.getVersion(),
                buildType = buildConfigRepository.getBuildType()
            )
        }
    }

    override fun processAction(action: AboutAppAction) {
        when (action) {
            AboutAppAction.OnBack -> miscRouter.back()
            AboutAppAction.TelegramClick -> {
                externalRouter.openUri("https://t.me/edugma_official")
            }
            AboutAppAction.VkClick -> {
                externalRouter.openUri("https://vk.com/edugma")
            }
            AboutAppAction.SourceCodeClick -> {
                externalRouter.openUri("https://github.com/Edugma/app")
            }
        }
    }
}
