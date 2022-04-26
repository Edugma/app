package io.edugma.android

import io.edugma.data.account.accountDataModule
import io.edugma.data.base.baseDataModule
import io.edugma.domain.account.accountDomainModule
import io.edugma.domain.base.baseDomainModule
import io.edugma.domain.schedule.scheduleDomainModule
import io.edugma.features.account.accountFeaturesModule
import io.edugma.features.base.core.baseFeaturesModule
import io.edugma.android.features.mainModule
import io.edugma.data.nodes.nodesDataModule
import io.edugma.features.misc.menu.miscMenuFeaturesModule
import io.edugma.features.misc.settings.settingsFeaturesModule
import io.edugma.features.nodes.nodesFeaturesModule

val appModules = listOf(
    mainModule,

    // Data modules
    baseDataModule,
    nodesDataModule,
    io.edugma.data.schedule.module,
    accountDataModule,


    // Domain modules
    baseDomainModule,
    scheduleDomainModule,
    accountDomainModule,


    // Features modules
    baseFeaturesModule,
    nodesFeaturesModule,
    io.edugma.features.schedule.module,
    io.edugma.features.schedule.schedule_info.module,
    io.edugma.features.schedule.calendar.module,
    io.edugma.features.schedule.lessons_review.module,
    io.edugma.features.schedule.sources.module,
    accountFeaturesModule,

    miscMenuFeaturesModule,
    settingsFeaturesModule,



    tempModule
)
