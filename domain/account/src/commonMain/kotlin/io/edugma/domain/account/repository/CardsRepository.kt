package io.edugma.domain.account.repository

import io.edugma.domain.account.model.menu.Card

interface CardsRepository {

    fun getCards(): List<List<Card>>
}
