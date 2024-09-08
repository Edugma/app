package com.edugma.features.misc.other.inAppUpdate

import com.edugma.core.api.consts.DiConst
import com.edugma.features.misc.other.inAppUpdate.data.InAppUpdateService
import com.edugma.features.misc.other.inAppUpdate.domain.CheckUpdateUseCase
import com.edugma.features.misc.other.inAppUpdate.domain.ParseSemVerUseCase
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val miscOtherFeaturesModule = module {
    single { get<Ktorfit>(named(DiConst.EdugmaStatic)).create<InAppUpdateService>() }
    factoryOf(::ParseSemVerUseCase)
    factoryOf(::CheckUpdateUseCase)
}
