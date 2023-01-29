package io.edugma.android

import io.edugma.features.account.accountScreens
import io.edugma.features.base.core.navigation.compose.groupScreen
import io.edugma.features.base.core.navigation.compose.screens
import io.edugma.features.base.navigation.MainScreen
import io.edugma.features.base.navigation.ScheduleScreens
import io.edugma.features.home.homeScreens
import io.edugma.features.misc.menu.miscMenuScreens
import io.edugma.features.nodes.nodesScreens
import io.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import io.edugma.features.schedule.daily.ScheduleDailyFeatureModule
import io.edugma.features.schedule.freePlace.ScheduleFreePlaceFeatureModule
import io.edugma.features.schedule.history.ScheduleHistoryFeatureModule
import io.edugma.features.schedule.lessonsReview.ScheduleLessonsReviewFeatureModule
import io.edugma.features.schedule.menu.ScheduleMenuFeatureModule
import io.edugma.features.schedule.schedule_info.ScheduleInfoFeatureModule
import io.edugma.features.schedule.sources.ScheduleSourcesFeatureModule

val appScreens = screens {
    nodesScreens()
    homeScreens()
    groupScreen<MainScreen.Schedule, ScheduleScreens.Menu> {
        ScheduleMenuFeatureModule.screens(this)
        ScheduleDailyFeatureModule.screens(this)
        ScheduleInfoFeatureModule.screens(this)
        ScheduleCalendarFeatureModule.screens(this)
        ScheduleLessonsReviewFeatureModule.screens(this)
        ScheduleSourcesFeatureModule.screens(this)
        ScheduleHistoryFeatureModule.screens(this)
        ScheduleFreePlaceFeatureModule.screens(this)
    }
    accountScreens()
    miscMenuScreens()
}
