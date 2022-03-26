package io.edugma.data.account

import io.edugma.data.account.api.AccountService
import io.edugma.data.account.repository.*
import io.edugma.data.base.consts.DiConst
import io.edugma.domain.account.repository.*
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val accountDataModule = module {
    single { get<Retrofit>(named(DiConst.Account)).create(AccountService::class.java) }
    single<ApplicationsRepository> { ApplicationsRepositoryImpl(get()) }
    single<PaymentsRepository> { PaymentsRepositoryImpl(get()) }
    single<PeoplesRepository> { PeoplesRepositoryImpl(get()) }
    single<PerformanceRepository> { PerformanceRepositoryImpl(get()) }
    single<PersonalRepository> { PersonalRepositoryImpl(get()) }
}