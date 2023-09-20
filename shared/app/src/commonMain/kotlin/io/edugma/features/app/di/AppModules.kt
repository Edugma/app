package io.edugma.features.app.di

import io.edugma.core.api.baseDomainModule
import io.edugma.core.system.coreSystemModule
import io.edugma.core.utils.coreUtilsModule
import io.edugma.data.base.baseDataModule
import io.edugma.data.schedule.ScheduleDataModule
import io.edugma.features.account.accountFeaturesModule
import io.edugma.features.app.main.mainModule
import io.edugma.features.misc.menu.miscMenuFeaturesModule
import io.edugma.features.misc.settings.settingsFeaturesModule
import io.edugma.features.nodes.nodesFeaturesModule
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
    +baseDataModule
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
    +coreModule
    +coreSystemModule
    +coreUtilsModule
}
