package com.edugma.features.app.core

import com.edugma.core.navigation.MainDestination
import com.edugma.core.navigation.ScheduleScreens
import com.edugma.features.account.accountScreens
import com.edugma.features.home.homeScreens
import com.edugma.features.misc.menu.miscMenuScreens
import com.edugma.features.nodes.nodesScreens
import com.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import com.edugma.features.schedule.daily.ScheduleDailyFeatureModule
import com.edugma.features.schedule.freePlace.ScheduleFreePlaceFeatureModule
import com.edugma.features.schedule.history.ScheduleHistoryFeatureModule
import com.edugma.features.schedule.lessonsReview.ScheduleLessonsReviewFeatureModule
import com.edugma.features.schedule.menu.ScheduleMenuFeatureModule
import com.edugma.features.schedule.scheduleInfo.ScheduleInfoFeatureModule
import com.edugma.features.schedule.sources.ScheduleSourcesFeatureModule
import com.edugma.navigation.core.graph.NavGraphBuilder
import com.edugma.navigation.core.graph.graph

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
