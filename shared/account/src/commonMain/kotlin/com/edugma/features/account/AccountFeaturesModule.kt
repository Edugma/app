package com.edugma.features.account

import com.edugma.core.api.repository.AuthInterceptorRepository
import com.edugma.features.account.accounts.AccountsViewModel
import com.edugma.features.account.addAccount.AddAccountViewModel
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.data.repository.AccountRepositoryImpl
import com.edugma.features.account.data.repository.ApplicationsRepositoryImpl
import com.edugma.features.account.data.repository.AuthInterceptorRepositoryImpl
import com.edugma.features.account.data.repository.AuthorizationRepositoryImpl
import com.edugma.features.account.data.repository.CardsRepositoryImpl
import com.edugma.features.account.data.repository.PaymentsRepositoryImpl
import com.edugma.features.account.data.repository.PeoplesRepositoryImpl
import com.edugma.features.account.data.repository.PerformanceRepositoryImpl
import com.edugma.features.account.data.repository.PersonalRepositoryImpl
import com.edugma.features.account.domain.repository.ApplicationsRepository
import com.edugma.features.account.domain.repository.AuthorizationRepository
import com.edugma.features.account.domain.repository.CardsRepository
import com.edugma.features.account.domain.repository.PaymentsRepository
import com.edugma.features.account.domain.repository.PeoplesRepository
import com.edugma.features.account.domain.repository.PerformanceRepository
import com.edugma.features.account.domain.repository.PersonalRepository
import com.edugma.features.account.domain.usecase.AuthWithCachingDataUseCase
import com.edugma.features.account.domain.usecase.MenuDataConverterUseCase
import com.edugma.features.account.menu.MenuViewModel
import com.edugma.features.account.payments.PaymentsViewModel
import com.edugma.features.account.people.presentation.PeopleViewModel
import com.edugma.features.account.performance.PerformanceViewModel
import com.edugma.features.account.personal.PersonalViewModel
import com.edugma.features.account.web.WebViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val accountFeaturesModule = module {
    singleOf(::AccountService)
    singleOf(::AccountRepositoryImpl)
    singleOf(::AuthInterceptorRepositoryImpl) { bind<AuthInterceptorRepository>() }
    singleOf(::ApplicationsRepositoryImpl) { bind<ApplicationsRepository>() }
    singleOf(::PaymentsRepositoryImpl) { bind<PaymentsRepository>() }
    singleOf(::PeoplesRepositoryImpl) { bind<PeoplesRepository>() }
    singleOf(::PerformanceRepositoryImpl) { bind<PerformanceRepository>() }
    singleOf(::PersonalRepositoryImpl) { bind<PersonalRepository>() }
    singleOf(::AuthorizationRepositoryImpl) { bind<AuthorizationRepository>() }
    singleOf(::CardsRepositoryImpl) { bind<CardsRepository>() }

    factoryOf(::AuthWithCachingDataUseCase)
    factoryOf(::MenuDataConverterUseCase)

    factoryOf(::MenuViewModel)
    factoryOf(::AccountsViewModel)
    factoryOf(::AddAccountViewModel)
    factoryOf(::PaymentsViewModel)
    factoryOf(::PeopleViewModel)
    factoryOf(::PerformanceViewModel)
    factoryOf(::PersonalViewModel)
    factoryOf(::WebViewModel)
}
