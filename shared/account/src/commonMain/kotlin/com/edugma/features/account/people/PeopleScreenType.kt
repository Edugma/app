package com.edugma.features.account.people

import androidx.compose.runtime.Immutable

@Immutable
data class PeopleScreenType(
    val url: String,
    val title: String,
    val queryHint: String,
    val hasSchedule: Boolean,
) {
    companion object {
        fun students() = PeopleScreenType(
            url = "account-peoples-students",
            title = "Студенты",
            queryHint = "Имя или группа",
            hasSchedule = true,
        )

        fun teachers() = PeopleScreenType(
            url = "account-peoples-teachers",
            title = "Преподаватели",
            queryHint = "Имя преподавателя",
            hasSchedule = true,
        )

        fun classmates() = PeopleScreenType(
            url = "account-peoples-classmates",
            title = "Одногруппники",
            queryHint = "Имя",
            hasSchedule = true,
        )
    }
}
