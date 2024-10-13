package com.edugma.features.misc.aboutApp

import androidx.compose.runtime.Immutable

@Immutable
data class AboutAppUiState(
    val version: String? = null,
    val buildType: String? = null,
)
