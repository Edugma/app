package io.edugma.features.base.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.features.base.core.navigation.compose.getFullRawRoute
import io.edugma.features.base.core.navigation.core.Screen
import io.edugma.features.base.navigation.misc.MiscMenuScreens
import io.edugma.features.navigation.R

sealed class MainScreen(
    @DrawableRes val iconId: Int,
    @DrawableRes val iconSelectedId: Int,
    @StringRes val resourceId: Int,
) : Screen() {
    abstract val route: String

    fun getIcon(selected: Boolean) =
        if (selected) iconSelectedId else iconId

    object Home : MainScreen(
        EdIcons.ic_fluent_home_24_regular,
        EdIcons.ic_fluent_home_24_filled,
        R.string.menu_home,
    ) {
        override val route: String = HomeScreens.Main.getFullRawRoute()
    }
    object Schedule : MainScreen(
        EdIcons.ic_fluent_calendar_ltr_24_regular,
        EdIcons.ic_fluent_calendar_ltr_24_filled,
        R.string.menu_schedule,
    ) {
        override val route: String = ScheduleScreens.Menu.getFullRawRoute()
    }
    object Account : MainScreen(
        EdIcons.ic_fluent_person_24_regular,
        EdIcons.ic_fluent_person_24_filled,
        R.string.menu_account,
    ) {
        override val route: String = AccountScreens.Menu.getFullRawRoute()
    }
    object Misc : MainScreen(
        EdIcons.ic_fluent_apps_24_regular,
        EdIcons.ic_fluent_apps_24_filled,
        R.string.menu_misc,
    ) {
        override val route: String = MiscMenuScreens.Menu.getFullRawRoute()
    }
}
