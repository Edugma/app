package io.edugma.features.app.core

import io.edugma.core.navigation.MainDestination
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.features.account.accountScreens
import io.edugma.features.home.homeScreens
import io.edugma.features.misc.menu.miscMenuScreens
import io.edugma.features.nodes.nodesScreens
import io.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import io.edugma.features.schedule.daily.ScheduleDailyFeatureModule
import io.edugma.features.schedule.freePlace.ScheduleFreePlaceFeatureModule
import io.edugma.features.schedule.history.ScheduleHistoryFeatureModule
import io.edugma.features.schedule.lessonsReview.ScheduleLessonsReviewFeatureModule
import io.edugma.features.schedule.menu.ScheduleMenuFeatureModule
import io.edugma.features.schedule.scheduleInfo.ScheduleInfoFeatureModule
import io.edugma.features.schedule.sources.ScheduleSourcesFeatureModule
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.graph.graph

fun NavGraphBuilder.appScreens() {
    nodesScreens()
    homeScreens()
    graph(MainDestination.Schedule, ScheduleScreens.Menu) {
        with(ScheduleMenuFeatureModule) { screens() }
        with(ScheduleDailyFeatureModule) { screens() }
        with(ScheduleInfoFeatureModule) { screens() }
        with(ScheduleCalendarFeatureModule) { screens() }
        with(ScheduleLessonsReviewFeatureModule) { screens() }
        with(ScheduleSourcesFeatureModule) { screens() }
        with(ScheduleHistoryFeatureModule) { screens() }
        with(ScheduleFreePlaceFeatureModule) { screens() }
    }
    accountScreens()
    miscMenuScreens()
}
