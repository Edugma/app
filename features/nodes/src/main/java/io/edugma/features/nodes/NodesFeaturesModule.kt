package io.edugma.features.nodes

import io.edugma.features.nodes.main.NodesMainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val nodesFeaturesModule = module {
    factoryOf(::NodesMainViewModel)
}
