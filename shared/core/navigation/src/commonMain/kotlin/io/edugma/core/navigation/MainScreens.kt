package io.edugma.core.navigation

import edugma.shared.core.icons.generated.resources.ic_fluent_apps_24_filled
import edugma.shared.core.icons.generated.resources.ic_fluent_apps_24_regular
import edugma.shared.core.icons.generated.resources.ic_fluent_calendar_ltr_24_filled
import edugma.shared.core.icons.generated.resources.ic_fluent_calendar_ltr_24_regular
import edugma.shared.core.icons.generated.resources.ic_fluent_home_24_filled
import edugma.shared.core.icons.generated.resources.ic_fluent_home_24_regular
import edugma.shared.core.icons.generated.resources.ic_fluent_person_24_filled
import edugma.shared.core.icons.generated.resources.ic_fluent_person_24_regular
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.icons.EdIcons
import io.edugma.navigation.core.screen.Screen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

sealed class MainScreen(
    name: String,
    val iconId: DrawableResource,
    val iconSelectedId: DrawableResource,
    val tabNameId: StringResource,
) : Screen(name) {

    fun getIcon(selected: Boolean) =
        if (selected) iconSelectedId else iconId

    object Home : MainScreen(
        name = "home",
        iconId = EdIcons.ic_fluent_home_24_regular,
        iconSelectedId = EdIcons.ic_fluent_home_24_filled,
        tabNameId = Res.string.menu_home,
    )

    object Schedule : MainScreen(
        name = "Schedule",
        iconId = EdIcons.ic_fluent_calendar_ltr_24_regular,
        iconSelectedId = EdIcons.ic_fluent_calendar_ltr_24_filled,
        tabNameId = Res.string.menu_schedule,
    )

    object Account : MainScreen(
        name = "Account",
        iconId = EdIcons.ic_fluent_person_24_regular,
        iconSelectedId = EdIcons.ic_fluent_person_24_filled,
        tabNameId = Res.string.menu_account,
    )

    object Misc : MainScreen(
        name = "Misc",
        iconId = EdIcons.ic_fluent_apps_24_regular,
        iconSelectedId = EdIcons.ic_fluent_apps_24_filled,
        tabNameId = Res.string.menu_misc,
    )
}
