package io.edugma.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.navigation.core.screen.Screen

sealed class MainScreen(
    name: String,
    @DrawableRes val iconId: Int,
    @DrawableRes val iconSelectedId: Int,
    @StringRes val tabNameId: Int,
) : Screen(name) {

    fun getIcon(selected: Boolean) =
        if (selected) iconSelectedId else iconId

    object Home : MainScreen(
        name = "home",
        iconId = EdIcons.ic_fluent_home_24_regular,
        iconSelectedId = EdIcons.ic_fluent_home_24_filled,
        tabNameId = R.string.menu_home,
    )

    object Schedule : MainScreen(
        name = "Schedule",
        iconId = EdIcons.ic_fluent_calendar_ltr_24_regular,
        iconSelectedId = EdIcons.ic_fluent_calendar_ltr_24_filled,
        tabNameId = R.string.menu_schedule,
    )

    object Account : MainScreen(
        name = "Account",
        iconId = EdIcons.ic_fluent_person_24_regular,
        iconSelectedId = EdIcons.ic_fluent_person_24_filled,
        tabNameId = R.string.menu_account,
    )

    object Misc : MainScreen(
        name = "Misc",
        iconId = EdIcons.ic_fluent_apps_24_regular,
        iconSelectedId = EdIcons.ic_fluent_apps_24_filled,
        tabNameId = R.string.menu_misc,
    )
}
