package com.edugma.features.account.data.repository

import com.edugma.features.account.domain.model.menu.Card
import com.edugma.features.account.domain.model.menu.CardType
import com.edugma.features.account.domain.repository.CardsRepository

class CardsRepositoryImpl : CardsRepository {
    override fun getCards(): List<List<Card>> {
        return listOf(
            listOf(
                Card(
                    id = "profile",
                    name = "Профиль",
                    label = null,
                    icon = "https://img.icons8.com/fluency/96/user-menu-male.png",
                    type = CardType.Profile,
                    weight = 0.5f,
                ),
                Card(
                    id = "marks",
                    name = "Оценки",
                    label = null,
                    icon = "https://img.icons8.com/fluency/96/null/school.png",
                    type = CardType.Marks,
                    weight = 0.5f,
                ),
            ),
            listOf(
                Card(
                    id = "payments",
                    name = "Оплаты",
                    label = null,
                    icon = "https://img.icons8.com/office/500/1FB141/card-in-use.png",
                    type = CardType.Payments,
                    weight = 0.5f,
                ),
                Card(
                    id = "Students",
                    name = "Студенты",
                    label = null,
                    icon = "https://img.icons8.com/external-flaticons-flat-flat-icons/512/external-student-professions-flaticons-flat-flat-icons.png",
                    type = CardType.Students,
                    weight = 0.5f,
                ),
            ),
            listOf(
                Card(
                    id = "teachers",
                    name = "Преподаватели",
                    label = null,
                    icon = "https://img.icons8.com/external-justicon-flat-justicon/512/external-teacher-elearning-and-education-justicon-flat-justicon.png",
                    type = CardType.Teachers,
                    weight = 0.5f,
                ),
                Card(
                    id = "classmates",
                    name = "Одногруппники",
                    label = null,
                    icon = "https://img.icons8.com/color/480/classmates-sittings-skin-type-1.png",
                    type = CardType.Classmates,
                    weight = 0.5f,
                ),
            ),
//            listOf(
//                Card(
//                    id = "pd",
//                    name = "Проектная деятельность",
//                    label = null,
//                    icon = "",
//                    type = CardType.Web,
//                    weight = 0.5f,
//                    url = "https://e.mospolytech.ru/#/project-activity",
//                ),
//            ),
        )
    }
}
