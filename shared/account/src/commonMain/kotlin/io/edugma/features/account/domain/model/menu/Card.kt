package io.edugma.features.account.domain.model.menu

@kotlinx.serialization.Serializable
data class Card(
    val id: String,
    val name: String,
    val label: String? = null,
    val icon: String,
    val type: CardType,
    val weight: Float,
    val url: String? = null,
)
