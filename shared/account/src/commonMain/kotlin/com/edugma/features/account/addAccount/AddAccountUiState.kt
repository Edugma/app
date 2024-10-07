package com.edugma.features.account.addAccount

import androidx.compose.runtime.Immutable

@Immutable
data class AddAccountUiState(
    val login: String = "",
    val loginError: Boolean = false,
    val password: String = "",
    val passwordError: Boolean = false,
    val error: String = "",
    val isLoading: Boolean = false,
    val passwordVisible: Boolean = false,
)
