package io.edugma.features.base.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.edugma.features.base.core.navigation.compose.getRoute
import io.edugma.features.base.core.navigation.core.Screen
import io.edugma.features.base.core.utils.FluentIcons
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
        FluentIcons.ic_fluent_home_24_regular,
        FluentIcons.ic_fluent_home_24_filled,
        R.string.menu_home,
    ) {
        override val route: String
            get() = getRoute()
    }
    object Schedule : MainScreen(
        FluentIcons.ic_fluent_calendar_ltr_24_regular,
        FluentIcons.ic_fluent_calendar_ltr_24_filled,
        R.string.menu_schedule,
    ) {
        override val route: String
            get() = getRoute()
    }
    object Account : MainScreen(
        FluentIcons.ic_fluent_person_24_regular,
        FluentIcons.ic_fluent_person_24_filled,
        R.string.menu_account,
    ) {
        override val route: String
            get() = getRoute()
    }
    object Misc : MainScreen(
        FluentIcons.ic_fluent_apps_24_regular,
        FluentIcons.ic_fluent_apps_24_filled,
        R.string.menu_misc,
    ) {
        override val route: String
            get() = getRoute()
    }
}
