package io.edugma.features.misc.other.inAppUpdate

import de.jensklingenberg.ktorfit.Ktorfit
import io.edugma.core.api.consts.DiConst
import io.edugma.features.misc.other.inAppUpdate.data.InAppUpdateService
import io.edugma.features.misc.other.inAppUpdate.domain.CheckUpdateUseCase
import io.edugma.features.misc.other.inAppUpdate.domain.ParseSemVerUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val miscOtherFeaturesModule = module {
    single { get<Ktorfit>(named(DiConst.EdugmaStatic)).create<InAppUpdateService>() }
    factoryOf(::ParseSemVerUseCase)
    factoryOf(::CheckUpdateUseCase)
}
