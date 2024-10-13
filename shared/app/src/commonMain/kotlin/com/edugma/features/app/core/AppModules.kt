package com.edugma.features.app.core

import com.edugma.core.api.baseDomainModule
import com.edugma.core.network.coreNetworkModule
import com.edugma.core.system.coreSystemModule
import com.edugma.core.utils.coreUtilsModule
import com.edugma.data.base.coreStorageModule
import com.edugma.data.schedule.ScheduleDataModule
import com.edugma.features.account.accountFeaturesModule
import com.edugma.features.misc.aboutApp.aboutAppFeaturesModule
import com.edugma.features.misc.menu.miscMenuFeaturesModule
import com.edugma.features.misc.other.inAppUpdate.miscOtherFeaturesModule
import com.edugma.features.misc.settings.settingsFeaturesModule
import com.edugma.features.nodes.nodesFeaturesModule
import com.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import com.edugma.features.schedule.daily.ScheduleDailyFeatureModule
import com.edugma.features.schedule.domain.ScheduleDomainModule
import com.edugma.features.schedule.elements.ScheduleElementsFeatureModule
import com.edugma.features.schedule.freePlace.ScheduleFreePlaceFeatureModule
import com.edugma.features.schedule.history.ScheduleHistoryFeatureModule
import com.edugma.features.schedule.lessonsReview.ScheduleLessonsReviewFeatureModule
import com.edugma.features.schedule.menu.ScheduleMenuFeatureModule
import com.edugma.features.schedule.scheduleInfo.ScheduleInfoFeatureModule
import com.edugma.features.schedule.sources.ScheduleSourcesFeatureModule
import org.koin.core.module.Module

val appModules: List<Module> = buildList {
    operator fun Module.unaryPlus() {
        add(this)
    }
    operator fun List<Module>.unaryPlus() {
        addAll(this)
    }

    +mainModule

    // Data modules
    +coreStorageModule
    +ScheduleDataModule.deps

    // Domain modules
    +baseDomainModule
    +ScheduleDomainModule.deps

    // Features modules
    +nodesFeaturesModule

    +ScheduleElementsFeatureModule.deps
    +ScheduleFreePlaceFeatureModule.deps
    +ScheduleMenuFeatureModule.deps
    +ScheduleDailyFeatureModule.deps
    +ScheduleInfoFeatureModule.deps
    +ScheduleCalendarFeatureModule.deps
    +ScheduleLessonsReviewFeatureModule.deps
    +ScheduleSourcesFeatureModule.deps
    +ScheduleHistoryFeatureModule.deps

    +accountFeaturesModule
    +miscMenuFeaturesModule
    +settingsFeaturesModule
    +aboutAppFeaturesModule
    +miscOtherFeaturesModule
    +coreModule
    +coreSystemModule
    +coreUtilsModule
    +coreNetworkModule
}
