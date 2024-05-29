package com.edugma.data.base

import com.edugma.core.api.consts.DiConst
import com.edugma.core.api.repository.BuildConfigRepository
import com.edugma.core.api.repository.CacheRepository
import com.edugma.core.api.repository.SettingsRepository
import com.edugma.core.network.buildKtorClient
import com.edugma.core.network.buildKtorfit
import com.edugma.core.network.interceptors.ApiVersionInterceptor
import com.edugma.core.network.interceptors.TokenInterceptor
import com.edugma.data.base.repository.CacheRepositoryImpl
import com.edugma.data.base.repository.EventRepository
import com.edugma.data.base.repository.SettingsRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreStorageModule = module {
    single { TokenInterceptor(get()) }
    single { ApiVersionInterceptor() }

    scheduleClient()
    accountClient()
    otherClient()
    edugmaStatic()

    single { EventRepository() }
    singleOf(::SettingsRepositoryImpl) { bind<SettingsRepository>() }
    singleOf(::CacheRepositoryImpl) { bind<CacheRepository>() }
} + coreStorageModulePlatform + coreStorageModulePlatform2

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
        )
    }
}

private fun Module.edugmaStatic() {
    single(named(DiConst.EdugmaStatic)) {
        val buildConfigRepository = get<BuildConfigRepository>()
        buildKtorClient(
            interceptors = listOf(),
            isLogsEnabled = buildConfigRepository.isNetworkLogsEnabled(),
        )
    }
    single(named(DiConst.EdugmaStatic)) {
        buildKtorfit(
            client = get(named(DiConst.EdugmaStatic)),
            baseUrl = "https://raw.githubusercontent.com/Edugma/resources/main/",
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
}
