package io.edugma.domain.account

import io.edugma.domain.account.usecase.AuthWithCachingDataUseCase
import io.edugma.domain.account.usecase.MenuDataConverterUseCase
import org.koin.dsl.module

val accountDomainModule = module {
    single { AuthWithCachingDataUseCase(get(), get(), get(), get()) }
    single { MenuDataConverterUseCase() }
}
