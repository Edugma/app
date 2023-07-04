package io.edugma.features.nodes

import de.jensklingenberg.ktorfit.Ktorfit
import io.edugma.data.base.consts.DiConst
import io.edugma.features.nodes.data.NodesRepositoryImpl
import io.edugma.features.nodes.data.NodesService
import io.edugma.features.nodes.domain.NodesRepository
import io.edugma.features.nodes.main.NodesMainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val nodesFeaturesModule = module {
    single { get<Ktorfit>(named(DiConst.OtherClient)).create<NodesService>() }
    single<NodesRepository> { NodesRepositoryImpl(get(), get(), get()) }
    factoryOf(::NodesMainViewModel)
}
