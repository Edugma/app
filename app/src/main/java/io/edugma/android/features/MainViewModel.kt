package io.edugma.android.features

import io.edugma.core.designSystem.utils.CommonImageLoader
import io.edugma.features.base.core.mvi.BaseViewModel

class MainViewModel(
    val commonImageLoader: CommonImageLoader,
    val iconImageLoader: CommonImageLoader,
) : BaseViewModel<Unit>(Unit)
