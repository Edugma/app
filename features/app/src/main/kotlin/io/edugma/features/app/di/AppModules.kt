package io.edugma.features.app.di

import io.edugma.data.account.accountDataModule
import io.edugma.data.base.baseDataModule
import io.edugma.data.nodes.nodesDataModule
import io.edugma.data.schedule.ScheduleDataModule
import io.edugma.domain.account.accountDomainModule
import io.edugma.domain.base.baseDomainModule
import io.edugma.features.account.accountFeaturesModule
import io.edugma.features.app.main.mainModule
import io.edugma.features.misc.menu.miscMenuFeaturesModule
import io.edugma.features.misc.settings.settingsFeaturesModule
import io.edugma.features.nodes.nodesFeaturesModule
import io.edugma.features.schedule.appwidget.ScheduleAppwidgetFeatureModule
import io.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import io.edugma.features.schedule.daily.ScheduleDailyFeatureModule
import io.edugma.features.schedule.domain.ScheduleDomainModule
import io.edugma.features.schedule.elements.ScheduleElementsFeatureModule
import io.edugma.features.schedule.freePlace.ScheduleFreePlaceFeatureModule
import io.edugma.features.schedule.history.ScheduleHistoryFeatureModule
import io.edugma.features.schedule.lessonsReview.ScheduleLessonsReviewFeatureModule
import io.edugma.features.schedule.menu.ScheduleMenuFeatureModule
import io.edugma.features.schedule.scheduleInfo.ScheduleInfoFeatureModule
import io.edugma.features.schedule.sources.ScheduleSourcesFeatureModule

val appModules = listOf(
    mainModule,

    // Data modules
    baseDataModule,
    nodesDataModule,
    ScheduleDataModule.deps,
    accountDataModule,

    // Domain modules
    baseDomainModule,
    ScheduleDomainModule.deps,
    accountDomainModule,

    // Features modules
    nodesFeaturesModule,

    ScheduleElementsFeatureModule.deps,
    ScheduleFreePlaceFeatureModule.deps,
    ScheduleMenuFeatureModule.deps,
    ScheduleDailyFeatureModule.deps,
    ScheduleInfoFeatureModule.deps,
    ScheduleCalendarFeatureModule.deps,
    ScheduleLessonsReviewFeatureModule.deps,
    ScheduleSourcesFeatureModule.deps,
    ScheduleAppwidgetFeatureModule.deps,
    ScheduleHistoryFeatureModule.deps,

    accountFeaturesModule,
    miscMenuFeaturesModule,
    settingsFeaturesModule,
    coreModule,
)
