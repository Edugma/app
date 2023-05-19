package io.edugma.data.account

import de.jensklingenberg.ktorfit.Ktorfit
import io.edugma.data.account.api.AccountService
import io.edugma.data.account.repository.ApplicationsRepositoryImpl
import io.edugma.data.account.repository.AuthorizationRepositoryImpl
import io.edugma.data.account.repository.CardsRepositoryImpl
import io.edugma.data.account.repository.PaymentsRepositoryImpl
import io.edugma.data.account.repository.PeoplesRepositoryImpl
import io.edugma.data.account.repository.PerformanceRepositoryImpl
import io.edugma.data.account.repository.PersonalRepositoryImpl
import io.edugma.data.base.consts.DiConst
import io.edugma.domain.account.repository.ApplicationsRepository
import io.edugma.domain.account.repository.AuthorizationRepository
import io.edugma.domain.account.repository.CardsRepository
import io.edugma.domain.account.repository.PaymentsRepository
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.account.repository.PersonalRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val accountDataModule = module {
    single { get<Ktorfit>(named(DiConst.Account)).create<AccountService>() }
    single<ApplicationsRepository> { ApplicationsRepositoryImpl(get(), get()) }
    single<PaymentsRepository> { PaymentsRepositoryImpl(get(), get()) }
    single<PeoplesRepository> { PeoplesRepositoryImpl(get(), get()) }
    single<PerformanceRepository> { PerformanceRepositoryImpl(get(), get()) }
    single<PersonalRepository> { PersonalRepositoryImpl(get(), get()) }
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get(), get()) }
    single<CardsRepository> { CardsRepositoryImpl() }
}
