package com.edugma.features.account.domain.usecase

import com.edugma.core.api.utils.first
import com.edugma.features.account.data.repository.AccountRepositoryImpl
import com.edugma.features.account.domain.model.Personal
import com.edugma.features.account.domain.model.accounts.AccountModel
import com.edugma.features.account.domain.model.payments.Contract
import com.edugma.features.account.domain.model.performance.GradePosition
import com.edugma.features.account.domain.repository.AuthorizationRepository
import com.edugma.features.account.domain.repository.PaymentsRepository
import com.edugma.features.account.domain.repository.PerformanceRepository
import com.edugma.features.account.domain.repository.PersonalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthWithCachingDataUseCase(
    private val authorizationRepository: AuthorizationRepository,
    private val personalRepository: PersonalRepository,
    private val paymentsRepository: PaymentsRepository,
    private val performanceRepository: PerformanceRepository,
    private val accountRepository: AccountRepositoryImpl,
) {

    suspend fun isAuthorized() = authorizationRepository.getAccessToken() != null

    suspend fun authorize(
        login: String,
        password: String,
    ) {
        val token = authorizationRepository.authorize(login, password)
        accountRepository.addNewAccountGroupFromToken(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
    }

    fun getDataFlow(): Flow<DataDto> = accountRepository.getSelectedAccount().map {
        // val marks = async { performanceRepository.getLocalMarks() ?: performanceRepository.getPerformance() }
        // val payments = async { paymentsRepository.getPaymentsLocal() ?: paymentsRepository.getPayments() }
        if (it == null) {
            val personal = personalRepository.getPersonalInfo().first().getOrNull()
            DataDto(
                selectedAccount = null,
                personal = personal,
                contracts = null,
                performance = null,
            )
        } else {
            DataDto(
                selectedAccount = it,
                personal = null,
                contracts = null,
                performance = null,
            )
        }
    }

    suspend fun logout() = authorizationRepository.logout()
}

data class DataDto(
    val selectedAccount: AccountModel?,
    val personal: Personal?,
    val contracts: List<Contract>?,
    val performance: List<GradePosition>?,
)
