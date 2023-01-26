package io.edugma.features.schedule.elements

import io.edugma.features.schedule.elements.vertical_schedule.VerticalScheduleViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object ScheduleElementsFeatureModule {
    val deps = module {
        viewModelOf(::VerticalScheduleViewModel)
    }
}
