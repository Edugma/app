package com.edugma.features.account.domain.usecase

import com.edugma.core.api.utils.IO
import com.edugma.core.api.utils.first
import com.edugma.features.account.data.repository.AccountRepositoryImpl
import com.edugma.features.account.domain.model.Personal
import com.edugma.features.account.domain.model.payments.Contract
import com.edugma.features.account.domain.model.performance.GradePosition
import com.edugma.features.account.domain.repository.AuthorizationRepository
import com.edugma.features.account.domain.repository.PaymentsRepository
import com.edugma.features.account.domain.repository.PerformanceRepository
import com.edugma.features.account.domain.repository.PersonalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

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
    ): DataDto {
        val token = authorizationRepository.authorize(login, password)
        accountRepository.addNewAccountGroupFromToken(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        return withContext(Dispatchers.IO) { getData() }
    }

    suspend fun getData(): DataDto =
        coroutineScope {
            val personal = async { personalRepository.getPersonalInfo().first().getOrNull() }
            // val marks = async { performanceRepository.getLocalMarks() ?: performanceRepository.getPerformance() }
            // val payments = async { paymentsRepository.getPaymentsLocal() ?: paymentsRepository.getPayments() }
            return@coroutineScope DataDto(personal = personal.await(), contracts = null, performance = null)
        }

    suspend fun logout() = authorizationRepository.logout()
}

data class DataDto(
    val personal: Personal?,
    val contracts: List<Contract>?,
    val performance: List<GradePosition>?,
)
