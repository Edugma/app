package io.edugma.android.features

import io.edugma.features.base.core.mvi.BaseViewModelFull

class MainViewModel : BaseViewModelFull<Unit, Nothing, Nothing>(
    Unit,
    { error(Unit) }
) {
}