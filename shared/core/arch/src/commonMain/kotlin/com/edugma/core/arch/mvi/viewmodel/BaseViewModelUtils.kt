package com.edugma.core.arch.mvi.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun <TAction> FeatureLogic<*, TAction>.rememberOnAction(): (TAction) -> Unit {
    return remember {
        {
            processAction(it)
        }
    }
}
