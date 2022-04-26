package io.edugma.android

import io.edugma.data.account.accountDataModule
import io.edugma.data.base.baseDataModule
import io.edugma.domain.account.accountDomainModule
import io.edugma.domain.base.baseDomainModule
import io.edugma.features.account.accountFeaturesModule
import io.edugma.features.base.core.baseFeaturesModule
import io.edugma.android.features.mainModule
import io.edugma.data.nodes.nodesDataModule
import io.edugma.data.schedule.ScheduleDataModule
import io.edugma.domain.schedule.ScheduleDomainModule
import io.edugma.features.misc.menu.miscMenuFeaturesModule
import io.edugma.features.misc.settings.settingsFeaturesModule
import io.edugma.features.nodes.nodesFeaturesModule
import io.edugma.features.schedule.ScheduleFeatureModule
import io.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import io.edugma.features.schedule.lessons_review.ScheduleLessonsReviewFeatureModule
import io.edugma.features.schedule.schedule_info.ScheduleInfoFeatureModule
import io.edugma.features.schedule.sources.ScheduleSourcesFeatureModule

val appModules = listOf(
    mainModule,

    // Data modules
    baseDataModule,
    nodesDataModule,
    ScheduleDataModule.module,
    accountDataModule,


    // Domain modules
    baseDomainModule,
    ScheduleDomainModule.module,
    accountDomainModule,


    // Features modules
    baseFeaturesModule,
    nodesFeaturesModule,

    ScheduleFeatureModule.module,
    ScheduleInfoFeatureModule.module,
    ScheduleCalendarFeatureModule.module,
    ScheduleLessonsReviewFeatureModule.module,
    ScheduleSourcesFeatureModule.module,

    accountFeaturesModule,
    miscMenuFeaturesModule,
    settingsFeaturesModule,



    tempModule
)
