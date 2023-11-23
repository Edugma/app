package io.edugma.core.network

import io.edugma.core.api.api.EdugmaHttpClient
import io.edugma.core.api.consts.DiConst
import io.edugma.core.api.repository.UrlRepository
import io.edugma.core.api.repository.UrlTemplateRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreNetworkModule = module {
    singleOf(::UrlRepositoryImpl) {
        bind<UrlRepository>()
        bind<UrlTemplateRepository>()
    }
    single<EdugmaHttpClient> { EdugmaHttpClientImpl(get(), get(named(DiConst.Account))) }
}
