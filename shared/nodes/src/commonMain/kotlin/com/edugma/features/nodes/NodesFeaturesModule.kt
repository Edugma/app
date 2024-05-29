package com.edugma.features.nodes

import de.jensklingenberg.ktorfit.Ktorfit
import com.edugma.core.api.consts.DiConst
import com.edugma.features.nodes.data.NodesRepositoryImpl
import com.edugma.features.nodes.data.NodesService
import com.edugma.core.api.repository.NodesRepository
import com.edugma.features.nodes.main.NodesMainViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val nodesFeaturesModule = module {
    single { get<Ktorfit>(named(DiConst.OtherClient)).create<NodesService>() }
    singleOf(::NodesRepositoryImpl) { bind<NodesRepository>() }
    factoryOf(::NodesMainViewModel)
}
