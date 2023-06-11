package io.edugma.android.features

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.designSystem.utils.CommonImageLoader

class MainViewModel(
    val commonImageLoader: CommonImageLoader,
    val iconImageLoader: CommonImageLoader,
) : BaseViewModel<Unit>(Unit)
