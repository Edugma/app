package io.edugma.data.base

import io.edugma.core.api.repository.BuildConfigRepository
import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.PreferenceRepository
import io.edugma.core.api.repository.SettingsRepository
import io.edugma.core.network.buildKtorClient
import io.edugma.core.network.buildKtorfit
import io.edugma.core.network.interceptors.ApiVersionInterceptor
import io.edugma.core.network.interceptors.TokenInterceptor
import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.repository.CacheRepositoryImpl
import io.edugma.data.base.repository.EventRepository
import io.edugma.data.base.repository.PreferenceRepositoryImpl
import io.edugma.data.base.repository.SettingsRepositoryImpl
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

    single { EventRepository() }
    factoryOf(::PreferenceRepositoryImpl) { bind<PreferenceRepository>() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    singleOf(::CacheRepositoryImpl) { bind<CacheRepository>() }
} + baseDataModulePlatform

private fun Module.otherClient() {
    single(named(DiConst.OtherClient)) {
        val buildConfigRepository = get<BuildConfigRepository>()
        buildKtorClient(
            interceptors = listOf(),
            isLogsEnabled = buildConfigRepository.isNetworkLogsEnabled(),
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
        val buildConfigRepository = get<BuildConfigRepository>()
        buildKtorClient(
            interceptors = listOf(
                get<TokenInterceptor>(),
                get<ApiVersionInterceptor>(),
            ),
            isLogsEnabled = buildConfigRepository.isNetworkLogsEnabled(),
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
        val buildConfigRepository = get<BuildConfigRepository>()
        buildKtorClient(
            interceptors = listOf(
                get<TokenInterceptor>(),
                get<ApiVersionInterceptor>(),
            ),
            isLogsEnabled = buildConfigRepository.isNetworkLogsEnabled(),
        )
    }
    single(named(DiConst.Schedule)) {
        buildKtorfit(
            client = get(named(DiConst.Schedule)),
            baseUrl = "http://devspare.mospolytech.ru:8003/",
        )
    }
}
