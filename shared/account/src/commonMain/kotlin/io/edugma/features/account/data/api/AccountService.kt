package io.edugma.features.account.data.api

import de.jensklingenberg.ktorfit.http.Body
import io.edugma.core.api.api.EdugmaHttpClient
import io.edugma.core.api.api.get
import io.edugma.core.api.api.getResult
import io.edugma.core.api.api.postResult
import io.edugma.core.api.model.PagingDto
import io.edugma.features.account.domain.model.Personal
import io.edugma.features.account.domain.model.applications.Application
import io.edugma.features.account.domain.model.auth.Login
import io.edugma.features.account.domain.model.auth.Token
import io.edugma.features.account.domain.model.payments.PaymentsDto
import io.edugma.features.account.domain.model.peoples.Person
import io.edugma.features.account.domain.model.performance.PerformanceDto

class AccountService(
    private val client: EdugmaHttpClient,
) {

    suspend fun login(@Body login: Login): Result<Token> =
        client.postResult("$PREFIX-login") {
            body(login)
        }

    suspend fun getLkToken(): Result<Token> =
        client.getResult("$PREFIX-lk-token")

    suspend fun getPeople(
        url: String,
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<Person> =
        client.get(url) {
            param("query", query)
            param("page", page)
            param("limit", limit)
        }

    suspend fun getApplications(): Result<List<Application>> =
        client.getResult("$PREFIX-applications")

    suspend fun getPerformance(periodId: String?): PerformanceDto =
        client.get("$PREFIX-performance") {
            param("periodId", periodId)
        }

    suspend fun getPersonalInfo(): Result<Personal> =
        client.getResult("$PREFIX-personal")

    suspend fun getPayments(contractId: String?): PaymentsDto =
        client.get("$PREFIX-payments") {
            param("contractId", contractId)
        }

    companion object {
        private const val PREFIX = "account"
    }
}
