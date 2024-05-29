package com.edugma.features.schedule.elements

import com.edugma.features.schedule.elements.verticalSchedule.VerticalScheduleViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScheduleElementsFeatureModule {
    val deps = module {
        factoryOf(::VerticalScheduleViewModel)
    }
}
