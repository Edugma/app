package io.edugma.data.base

import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.local.CacheLocalDS
import io.edugma.data.base.local.CacheVersionLocalDS
import io.edugma.data.base.local.DataVersionLocalDS
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.PreferencesLocalDS
import io.edugma.data.base.repository.EventRepository
import io.edugma.data.base.utils.PathProvider
import io.edugma.data.base.utils.ktorfit.buildKtorClient
import io.edugma.data.base.utils.ktorfit.buildKtorfit
import io.edugma.data.base.utils.ktorfit.interceptors.ApiVersionInterceptor
import io.edugma.data.base.utils.ktorfit.interceptors.TokenInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val baseDataModule = module {
    single { PathProvider(get()) }
    single { TokenInterceptor(get()) }
    single { ApiVersionInterceptor() }

    scheduleClient()
    accountClient()
    otherClient()

    single { EventRepository() }
    single { DataVersionLocalDS(get()) }
    single<PreferencesDS> { PreferencesLocalDS(get()) }
    single { CacheLocalDS(get()) }
    single { CacheVersionLocalDS(get(), get()) }
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
