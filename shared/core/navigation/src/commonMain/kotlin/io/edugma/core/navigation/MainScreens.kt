package io.edugma.core.navigation

import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import io.edugma.core.icons.EdIcons
import io.edugma.navigation.core.screen.Screen

sealed class MainScreen(
    name: String,
    val iconId: ImageResource,
    val iconSelectedId: ImageResource,
    val tabNameId: StringResource,
) : Screen(name) {

    fun getIcon(selected: Boolean) =
        if (selected) iconSelectedId else iconId

    object Home : MainScreen(
        name = "home",
        iconId = EdIcons.ic_fluent_home_24_regular,
        iconSelectedId = EdIcons.ic_fluent_home_24_filled,
        tabNameId = MR.strings.menu_home,
    )

    object Schedule : MainScreen(
        name = "Schedule",
        iconId = EdIcons.ic_fluent_calendar_ltr_24_regular,
        iconSelectedId = EdIcons.ic_fluent_calendar_ltr_24_filled,
        tabNameId = MR.strings.menu_schedule,
    )

    object Account : MainScreen(
        name = "Account",
        iconId = EdIcons.ic_fluent_person_24_regular,
        iconSelectedId = EdIcons.ic_fluent_person_24_filled,
        tabNameId = MR.strings.menu_account,
    )

    object Misc : MainScreen(
        name = "Misc",
        iconId = EdIcons.ic_fluent_apps_24_regular,
        iconSelectedId = EdIcons.ic_fluent_apps_24_filled,
        tabNameId = MR.strings.menu_misc,
    )
}
