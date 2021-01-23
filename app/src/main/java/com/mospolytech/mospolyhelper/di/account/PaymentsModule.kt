package com.mospolytech.mospolyhelper.di.account

import com.mospolytech.mospolyhelper.data.account.info.api.InfoHerokuClient
import com.mospolytech.mospolyhelper.data.account.info.local.InfoLocalDataSource
import com.mospolytech.mospolyhelper.data.account.info.remote.InfoRemoteDataSource
import com.mospolytech.mospolyhelper.data.account.info.repository.InfoRepositoryImpl
import com.mospolytech.mospolyhelper.data.account.payments.api.PaymentsHerokuClient
import com.mospolytech.mospolyhelper.data.account.payments.local.PaymentsLocalDataSource
import com.mospolytech.mospolyhelper.data.account.payments.remote.PaymentsRemoteDataSource
import com.mospolytech.mospolyhelper.data.account.payments.repository.PaymentsRepositoryImpl
import com.mospolytech.mospolyhelper.domain.account.info.repository.InfoRepository
import com.mospolytech.mospolyhelper.domain.account.info.usecase.InfoUseCase
import com.mospolytech.mospolyhelper.domain.account.payments.repository.PaymentsRepository
import com.mospolytech.mospolyhelper.domain.account.payments.usecase.PaymentsUseCase
import com.mospolytech.mospolyhelper.features.ui.account.info.InfoViewModel
import com.mospolytech.mospolyhelper.features.ui.account.payments.PaymentsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val paymentsModule = module {
    single { PaymentsHerokuClient(get(named("accountHerokuClient"))) }
    single { PaymentsRemoteDataSource(get()) }
    single { PaymentsLocalDataSource(get()) }
    single<PaymentsRepository> { PaymentsRepositoryImpl(get(), get(), get()) }
    single { PaymentsUseCase(get()) }
    viewModel { PaymentsViewModel(get(), get()) }
}