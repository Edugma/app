package io.edugma.features.account.personal

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.LceUiState
import io.edugma.features.account.domain.model.Personal

@Immutable
data class PersonalUiState(
    val lceState: LceUiState = LceUiState.init(),
    val personal: Personal? = null,
) {
    fun toContent(personal: Personal) = copy(
        personal = personal,
    )
}
