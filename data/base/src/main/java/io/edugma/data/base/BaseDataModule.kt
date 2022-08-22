package io.edugma.data.base

import io.edugma.data.base.consts.DiConst
import io.edugma.data.base.local.*
import io.edugma.data.base.repository.EventRepository
import io.edugma.data.base.utils.PathProvider
import io.edugma.data.base.utils.retrofit.buildRetrofitBuilder
import io.edugma.data.base.utils.retrofit.interceptors.ApiVersionInterceptor
import io.edugma.data.base.utils.retrofit.interceptors.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    single { OkHttpClient.Builder() }
    single {
        get<OkHttpClient.Builder>()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(get<TokenInterceptor>())
            .addInterceptor(get<ApiVersionInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named(DiConst.Schedule)) {
        buildRetrofitBuilder(get(), "http://devspare.mospolytech.ru:8002/")
    }
    single<Retrofit>(named(DiConst.Schedule)) { get<Retrofit.Builder>(named(DiConst.Schedule)).build() }


    single(named(DiConst.Account)) {
        buildRetrofitBuilder(get(), "http://devspare.mospolytech.ru:8003/")
    }
    single<Retrofit>(named(DiConst.Account)) { get<Retrofit.Builder>(named(DiConst.Account)).build() }

    single { EventRepository() }
    single { DataVersionLocalDS(get()) }
    single<PreferencesDS> { PreferencesLocalDS(get()) }
    single { CacheLocalDS(get()) }
    single { CacheVersionLocalDS(get(), get()) }
}