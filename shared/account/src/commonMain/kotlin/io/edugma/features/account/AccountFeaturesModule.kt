package io.edugma.features.account

import de.jensklingenberg.ktorfit.Ktorfit
import io.edugma.data.base.consts.DiConst
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.data.repository.ApplicationsRepositoryImpl
import io.edugma.features.account.data.repository.AuthorizationRepositoryImpl
import io.edugma.features.account.data.repository.CardsRepositoryImpl
import io.edugma.features.account.data.repository.PaymentsRepositoryImpl
import io.edugma.features.account.data.repository.PeoplesRepositoryImpl
import io.edugma.features.account.data.repository.PerformanceRepositoryImpl
import io.edugma.features.account.data.repository.PersonalRepositoryImpl
import io.edugma.features.account.domain.repository.ApplicationsRepository
import io.edugma.features.account.domain.repository.AuthorizationRepository
import io.edugma.features.account.domain.repository.CardsRepository
import io.edugma.features.account.domain.repository.PaymentsRepository
import io.edugma.features.account.domain.repository.PeoplesRepository
import io.edugma.features.account.domain.repository.PerformanceRepository
import io.edugma.features.account.domain.repository.PersonalRepository
import io.edugma.features.account.domain.usecase.AuthWithCachingDataUseCase
import io.edugma.features.account.domain.usecase.MenuDataConverterUseCase
import io.edugma.features.account.marks.PerformanceViewModel
import io.edugma.features.account.menu.MenuViewModel
import io.edugma.features.account.payments.PaymentsViewModel
import io.edugma.features.account.people.classmates.ClassmatesViewModel
import io.edugma.features.account.people.students.StudentsViewModel
import io.edugma.features.account.people.teachers.TeachersViewModel
import io.edugma.features.account.personal.PersonalViewModel
import io.edugma.features.account.web.WebViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val accountFeaturesModule = module {
    single { get<Ktorfit>(named(DiConst.Account)).create<AccountService>() }
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
    factoryOf(::ClassmatesViewModel)
    factoryOf(::PaymentsViewModel)
    factoryOf(::StudentsViewModel)
    factoryOf(::PerformanceViewModel)
    factoryOf(::PersonalViewModel)
    factoryOf(::TeachersViewModel)
    factoryOf(::WebViewModel)
}
