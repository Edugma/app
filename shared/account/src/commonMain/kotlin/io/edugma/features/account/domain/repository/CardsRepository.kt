package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.menu.Card

interface CardsRepository {

    fun getCards(): List<List<Card>>
}
