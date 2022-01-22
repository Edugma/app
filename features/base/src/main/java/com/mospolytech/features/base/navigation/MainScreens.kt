package com.mospolytech.features.base.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mospolytech.features.base.R
import com.mospolytech.features.base.navigation.core.Screen

sealed class MainScreen(
    route: String,
    @DrawableRes val iconId: Int,
    @DrawableRes val iconSelectedId: Int,
    @StringRes val resourceId: Int
) : Screen(route) {
    fun getIcon(selected: Boolean) =
        if (selected) iconSelectedId else iconId

    object Home : MainScreen(
        "home",
        R.drawable.ic_fluent_home_24_regular,
        R.drawable.ic_fluent_home_24_filled,
        R.string.menu_home
    )
    object Schedule : MainScreen(
        "schedule",
        R.drawable.ic_fluent_calendar_ltr_24_regular,
        R.drawable.ic_fluent_calendar_ltr_24_filled,
        R.string.menu_schedule
    )
    object Account : MainScreen(
        "account",
        R.drawable.ic_fluent_person_24_regular,
        R.drawable.ic_fluent_person_24_filled,
        R.string.menu_account
    )
    object Misc : MainScreen(
        "menu",
        R.drawable.ic_fluent_apps_24_regular,
        R.drawable.ic_fluent_apps_24_filled,
        R.string.menu_misc
    )
}

