package io.edugma.android

import io.edugma.data.account.accountDataModule
import io.edugma.data.base.baseDataModule
import io.edugma.data.schedule.scheduleDataModule
import io.edugma.domain.account.accountDomainModule
import io.edugma.domain.base.baseDomainModule
import io.edugma.domain.schedule.scheduleDomainModule
import io.edugma.features.account.accountFeaturesModule
import io.edugma.features.base.core.baseFeaturesModule
import io.edugma.features.schedule.scheduleFeaturesModule
import io.edugma.android.features.mainModule
import io.edugma.data.nodes.nodesDataModule

val appModules = listOf(
    mainModule,

    // Data modules
    baseDataModule,
    nodesDataModule,
    scheduleDataModule,
    accountDataModule,


    // Domain modules
    baseDomainModule,
    scheduleDomainModule,
    accountDomainModule,


    // Features modules
    baseFeaturesModule,
    scheduleFeaturesModule,
    accountFeaturesModule,



    tempModule
)
