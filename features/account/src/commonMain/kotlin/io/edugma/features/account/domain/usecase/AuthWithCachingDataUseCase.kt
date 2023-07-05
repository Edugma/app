package io.edugma.features.account.domain.usecase

import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.model.Personal
import io.edugma.features.account.domain.model.Contracts
import io.edugma.features.account.domain.repository.AuthorizationRepository
import io.edugma.features.account.domain.repository.PaymentsRepository
import io.edugma.features.account.domain.repository.PerformanceRepository
import io.edugma.features.account.domain.repository.PersonalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class AuthWithCachingDataUseCase(
    private val authorizationRepository: AuthorizationRepository,
    private val personalRepository: PersonalRepository,
    private val paymentsRepository: PaymentsRepository,
    private val performanceRepository: PerformanceRepository,
) {

    suspend fun isAuthorized() = authorizationRepository.getSavedToken() != null

    suspend fun authorize(
        login: String,
        password: String,
        onAuthSuccess: () -> Unit,
        onAuthFailure: (Throwable) -> Unit,
        onGetData: (DataDto) -> Unit,
    ) {
        val authResult = authorizationRepository.authorizationSuspend(login, password)
            .onFailure { onAuthFailure(it) }
            .onSuccess { onAuthSuccess() }
        if (authResult.isFailure) return
        withContext(Dispatchers.IO) { onGetData(getData()) }
    }

    suspend fun getData(): DataDto =
        coroutineScope {
            val personal = async { personalRepository.getLocalPersonalInfo() ?: personalRepository.getPersonalInfoSuspend().getOrNull() }
            val marks = async { performanceRepository.getLocalMarks() ?: performanceRepository.getMarksBySemesterSuspend().getOrNull() }
            val payments = async { paymentsRepository.getPaymentsLocal() ?: paymentsRepository.getPaymentsSuspend().getOrNull() }
            return@coroutineScope DataDto(personal = personal.await(), contracts = payments.await(), performance = marks.await())
        }

    suspend fun logout() = authorizationRepository.logout()
}

data class DataDto(
    val personal: Personal?,
    val contracts: io.edugma.features.account.domain.model.Contracts?,
    val performance: List<Performance>?,
)
