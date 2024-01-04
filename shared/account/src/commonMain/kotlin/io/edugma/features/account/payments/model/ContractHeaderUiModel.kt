package io.edugma.features.account.payments.model

import androidx.compose.runtime.Immutable
import io.edugma.core.api.model.ListItemUiModel
import io.edugma.features.account.domain.model.payments.ContractHeader

@Immutable
data class ContractHeaderUiModel(
    val id: String,
    val title: String,
) : ListItemUiModel() {
    override val listContentType: Any = 0
}

fun ContractHeader.toUiModel(): ContractHeaderUiModel {
    return ContractHeaderUiModel(
        id = id,
        title = title,
    )
}
