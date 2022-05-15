package io.edugma.android

import io.edugma.data.account.accountDataModule
import io.edugma.data.base.baseDataModule
import io.edugma.domain.account.accountDomainModule
import io.edugma.domain.base.baseDomainModule
import io.edugma.features.account.accountFeaturesModule
import io.edugma.features.base.core.baseFeaturesModule
import io.edugma.android.features.mainModule
import io.edugma.data.base.model.DataVersion
import io.edugma.data.base.utils.PathProvider
import io.edugma.data.base.utils.buildDB
import io.edugma.data.nodes.nodesDataModule
import io.edugma.data.schedule.ScheduleDataModule
import io.edugma.data.schedule.model.ScheduleDao
import io.edugma.data.schedule.model.ScheduleSourceDao
import io.edugma.data.schedule.model.ScheduleSourceFullDao
import io.edugma.domain.schedule.ScheduleDomainModule
import io.edugma.features.misc.menu.miscMenuFeaturesModule
import io.edugma.features.misc.settings.settingsFeaturesModule
import io.edugma.features.nodes.nodesFeaturesModule
import io.edugma.features.schedule.ScheduleFeatureModule
import io.edugma.features.schedule.appwidget.ScheduleAppwidgetModule
import io.edugma.features.schedule.calendar.ScheduleCalendarFeatureModule
import io.edugma.features.schedule.lessons_review.ScheduleLessonsReviewFeatureModule
import io.edugma.features.schedule.schedule_info.ScheduleInfoFeatureModule
import io.edugma.features.schedule.sources.ScheduleSourcesFeatureModule
import org.kodein.db.DB
import org.kodein.db.OpenPolicy
import org.kodein.db.impl.open
import org.kodein.db.orm.kotlinx.KotlinxSerializer
import org.koin.dsl.module
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.pathString

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
    baseFeaturesModule,
    nodesFeaturesModule,

    ScheduleFeatureModule.deps,
    ScheduleInfoFeatureModule.deps,
    ScheduleCalendarFeatureModule.deps,
    ScheduleLessonsReviewFeatureModule.deps,
    ScheduleSourcesFeatureModule.deps,
    ScheduleAppwidgetModule.deps,

    accountFeaturesModule,
    miscMenuFeaturesModule,
    settingsFeaturesModule,




    // TODO: Temp
    module {
        single {
            buildDB(get(), KotlinxSerializer {
                +ScheduleDao.serializer()
                +ScheduleSourceDao.serializer()
                +DataVersion.serializer()
                +ScheduleSourceFullDao.serializer()
            })
        }
    }
)
