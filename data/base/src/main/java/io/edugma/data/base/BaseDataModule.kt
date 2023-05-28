package io.edugma.data.base

import io.edugma.core.network.buildKtorClient
import io.edugma.core.network.buildKtorfit
import io.edugma.core.network.interceptors.ApiVersionInterceptor
import io.edugma.core.network.interceptors.TokenInterceptor
import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.repository.CacheRepositoryImpl
import io.edugma.data.base.repository.EventRepository
import io.edugma.data.base.repository.PathRepositoryImpl
import io.edugma.data.base.repository.PreferenceRepositoryImpl
import io.edugma.data.base.repository.SettingsRepositoryImpl
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.PathRepository
import io.edugma.domain.base.repository.PreferenceRepository
import io.edugma.domain.base.repository.SettingsRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val baseDataModule = module {
    single { TokenInterceptor(get()) }
    single { ApiVersionInterceptor() }

    scheduleClient()
    accountClient()
    otherClient()

    singleOf(::PathRepositoryImpl) { bind<PathRepository>() }
    single { EventRepository() }
    factoryOf(::PreferenceRepositoryImpl) { bind<PreferenceRepository>() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    singleOf(::CacheRepositoryImpl) { bind<CacheRepository>() }
}

private fun Module.otherClient() {
    single(named(DiConst.OtherClient)) {
        buildKtorClient(
            listOf(),
        )
    }
    single(named(DiConst.OtherClient)) {
        buildKtorfit(
            client = get(named(DiConst.OtherClient)),
            baseUrl = "http://devspare.mospolytech.ru:8003/",
        )
    }
}

private fun Module.accountClient() {
    single(named(DiConst.Account)) {
        buildKtorClient(
            listOf(
                get<TokenInterceptor>(),
                get<ApiVersionInterceptor>(),
            ),
        )
    }
    single(named(DiConst.Account)) {
        buildKtorfit(
            client = get(named(DiConst.Account)),
            baseUrl = "http://devspare.mospolytech.ru:8003/",
        )
    }
}

private fun Module.scheduleClient() {
    single(named(DiConst.Schedule)) {
        buildKtorClient(
            listOf(
                get<TokenInterceptor>(),
                get<ApiVersionInterceptor>(),
            ),
        )
    }
    single(named(DiConst.Schedule)) {
        buildKtorfit(
            client = get(named(DiConst.Schedule)),
            baseUrl = "http://devspare.mospolytech.ru:8003/",
        )
    }
}
