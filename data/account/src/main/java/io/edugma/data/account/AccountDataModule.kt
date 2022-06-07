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
    single<ApplicationsRepository> { ApplicationsRepositoryImpl(get(), get()) }
    single<PaymentsRepository> { PaymentsRepositoryImpl(get(), get()) }
    single<PeoplesRepository> { PeoplesRepositoryImpl(get(), get()) }
    single<PerformanceRepository> { PerformanceRepositoryImpl(get(), get()) }
    single<PersonalRepository> { PersonalRepositoryImpl(get(), get()) }
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get(), get()) }
}