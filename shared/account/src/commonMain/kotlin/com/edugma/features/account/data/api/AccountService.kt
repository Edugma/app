package com.edugma.features.account.data.api

import com.edugma.core.api.api.EdugmaHttpClient
import com.edugma.core.api.api.get
import com.edugma.core.api.api.getResult
import com.edugma.core.api.api.postResult
import com.edugma.core.api.model.PagingDto
import com.edugma.features.account.domain.model.Personal
import com.edugma.features.account.domain.model.accounts.AccountsModel
import com.edugma.features.account.domain.model.applications.Application
import com.edugma.features.account.domain.model.auth.Login
import com.edugma.features.account.domain.model.auth.Token
import com.edugma.features.account.domain.model.payments.PaymentsDto
import com.edugma.features.account.domain.model.peoples.Person
import com.edugma.features.account.domain.model.performance.PerformanceDto
import de.jensklingenberg.ktorfit.http.Body

class AccountService(
    private val client: EdugmaHttpClient,
) {

    suspend fun login(@Body login: Login): Result<Token> =
        client.postResult("$PREFIX-login") {
            body(login)
        }

    suspend fun getAccounts(): AccountsModel =
        client.get("$PREFIX-accounts")

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
        client.get<PerformanceDto>("$PREFIX-performance") {
            param("periodId", periodId)
        }

    suspend fun getPersonalInfo(): Personal =
        client.get("$PREFIX-personal")

    suspend fun getPayments(contractId: String?): PaymentsDto =
        client.get("$PREFIX-payments") {
            param("contractId", contractId)
        }

    companion object {
        private const val PREFIX = "account"
    }
}
