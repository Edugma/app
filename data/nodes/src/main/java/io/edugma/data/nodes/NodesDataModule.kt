package io.edugma.data.nodes

import de.jensklingenberg.ktorfit.Ktorfit
import io.edugma.data.base.consts.DiConst
import io.edugma.data.nodes.api.NodesService
import io.edugma.data.nodes.repository.NodesRepositoryImpl
import io.edugma.domain.nodes.repository.NodesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val nodesDataModule = module {
    single { get<Ktorfit>(named(DiConst.OtherClient)).create<NodesService>() }
    single<NodesRepository> { NodesRepositoryImpl(get(), get(), get()) }
}
