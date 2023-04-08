package io.edugma.data.account.repository

import io.edugma.domain.account.model.menu.Card
import io.edugma.domain.account.model.menu.CardType
import io.edugma.domain.account.repository.CardsRepository

class CardsRepositoryImpl: CardsRepository {
    override fun getCards(): List<List<Card>> {
        return listOf(
            listOf(
                Card(
                    id = "marks",
                    name = "Оценки",
                    label = null,
                    icon = "",
                    type = CardType.Marks,
                    weight = 0.6f,
                ),
                Card(
                    id = "payments",
                    name = "Оплаты",
                    label = null,
                    icon = "",
                    type = CardType.Payments,
                    weight = 0.3f,
                ),
            ),
            listOf(
                Card(
                    id = "Students",
                    name = "Студенты",
                    label = null,
                    icon = "",
                    type = CardType.Students,
                    weight = 0.3f,
                ),
                Card(
                    id = "teachers",
                    name = "Преподаватели",
                    label = null,
                    icon = "",
                    type = CardType.Teachers,
                    weight = 0.3f,
                ),
                Card(
                    id = "classmates",
                    name = "Одногруппники",
                    label = null,
                    icon = "",
                    type = CardType.Classmates,
                    weight = 0.3f,
                ),
            ),
            listOf(
                Card(
                    id = "pd",
                    name = "Проектная деятельность",
                    label = null,
                    icon = "",
                    type = CardType.Web,
                    weight = 0.5f,
                    url = "https://e.mospolytech.ru/#/project-activity"
                ),
            ),
        )
    }
}
