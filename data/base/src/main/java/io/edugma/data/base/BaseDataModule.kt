package io.edugma.data.base

import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.local.CacheLocalDS
import io.edugma.data.base.local.CacheVersionLocalDS
import io.edugma.data.base.local.DataVersionLocalDS
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.PreferencesLocalDS
import io.edugma.data.base.repository.EventRepository
import io.edugma.data.base.utils.PathProvider
import io.edugma.data.base.utils.retrofit.buildRetrofitBuilder
import io.edugma.data.base.utils.retrofit.interceptors.ApiVersionInterceptor
import io.edugma.data.base.utils.retrofit.interceptors.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val baseDataModule = module {
    single { PathProvider(get()) }
    single { TokenInterceptor(get()) }
    single { ApiVersionInterceptor() }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

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
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single(named(DiConst.OtherClient)) {
        buildRetrofitBuilder(
            get(named(DiConst.OtherClient)),
            "http://devspare.mospolytech.ru:8003/",
        )
    }
    single<Retrofit>(named(DiConst.OtherClient)) { get<Retrofit.Builder>(named(DiConst.OtherClient)).build() }
}

private fun Module.accountClient() {
    single(named(DiConst.Account)) {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(get<TokenInterceptor>())
            .addInterceptor(get<ApiVersionInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single(named(DiConst.Account)) {
        buildRetrofitBuilder(get(named(DiConst.Account)), "http://devspare.mospolytech.ru:8003/")
    }
    single<Retrofit>(named(DiConst.Account)) { get<Retrofit.Builder>(named(DiConst.Account)).build() }
}

private fun Module.scheduleClient() {
    single(named(DiConst.Schedule)) {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(get<TokenInterceptor>())
            .addInterceptor(get<ApiVersionInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single(named(DiConst.Schedule)) {
        buildRetrofitBuilder(get(named(DiConst.Schedule)), "http://devspare.mospolytech.ru:8003/")
    }
    single<Retrofit>(named(DiConst.Schedule)) { get<Retrofit.Builder>(named(DiConst.Schedule)).build() }
}
