package com.edugma.features.account.domain.repository

import com.edugma.features.account.domain.model.menu.Card

interface CardsRepository {

    fun getCards(): List<List<Card>>
}
