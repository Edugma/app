package com.edugma.features.misc.other.inAppUpdate

import com.edugma.core.api.consts.DiConst
import com.edugma.features.misc.other.inAppUpdate.data.InAppUpdateService
import com.edugma.features.misc.other.inAppUpdate.domain.CheckUpdateUseCase
import com.edugma.features.misc.other.inAppUpdate.domain.ParseSemVerUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val miscOtherFeaturesModule = module {
    single { InAppUpdateService(get(named(DiConst.EdugmaStatic))) }
    factoryOf(::ParseSemVerUseCase)
    factoryOf(::CheckUpdateUseCase)
}
