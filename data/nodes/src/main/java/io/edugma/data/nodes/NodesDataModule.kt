package io.edugma.data.nodes

import io.edugma.data.base.consts.DiConst
import io.edugma.data.nodes.api.NodesService
import io.edugma.data.nodes.repository.NodesRepositoryImpl
import io.edugma.domain.nodes.repository.NodesRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val nodesDataModule = module {
    single { get<Retrofit>(named(DiConst.Schedule)).create(NodesService::class.java) }
    single<NodesRepository> { NodesRepositoryImpl(get(), get(), get()) }
}
