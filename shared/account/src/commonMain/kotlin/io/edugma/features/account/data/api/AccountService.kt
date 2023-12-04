package io.edugma.features.account.data.api

import de.jensklingenberg.ktorfit.http.Body
import io.edugma.core.api.api.EdugmaHttpClient
import io.edugma.core.api.api.get
import io.edugma.core.api.api.getResult
import io.edugma.core.api.api.postResult
import io.edugma.core.api.model.PagingDto
import io.edugma.features.account.domain.model.Personal
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.model.applications.Application
import io.edugma.features.account.domain.model.auth.Login
import io.edugma.features.account.domain.model.auth.Token
import io.edugma.features.account.domain.model.payments.Contracts
import io.edugma.features.account.domain.model.payments.PaymentType
import io.edugma.features.account.domain.model.performance.Performance
import io.edugma.features.account.domain.model.performance.SemestersWithCourse
import io.edugma.features.account.domain.model.student.Student

class AccountService(
    private val client: EdugmaHttpClient,
) {

    suspend fun login(@Body login: Login): Result<Token> =
        client.postResult("$PREFIX-login") {
            body(login)
        }

    suspend fun getLkToken(): Result<Token> =
        client.getResult("$PREFIX-lk-token")

    suspend fun getClassmates(): Result<List<Student>> =
        client.getResult("$PREFIX-peoples-classmates")

    suspend fun getStudents(
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<Student> =
        client.get("$PREFIX-peoples-students") {
            param("query", query)
            param("page", page)
            param("limit", limit)
        }

    suspend fun getTeachers(
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<Teacher> =
        client.get("$PREFIX-peoples-teachers") {
            param("query", query)
            param("page", page)
            param("limit", limit)
        }

    suspend fun getApplications(): Result<List<Application>> =
        client.getResult("$PREFIX-applications")

    suspend fun getSemesters(): Result<List<Int>> =
        client.getResult("$PREFIX-performance-semesters")

    suspend fun getCoursesWithSemesters(): Result<SemestersWithCourse> =
        client.getResult("$PREFIX-performance-courses-semesters")

    suspend fun getMarks(semester: String): Result<List<Performance>> =
        client.getResult("$PREFIX-performance-semester") {
            param("semester", semester)
        }

    suspend fun getCourses(): Result<List<Int>> =
        client.getResult("$PREFIX-performance-courses")

    suspend fun getPersonalInfo(): Result<Personal> =
        client.getResult("$PREFIX-personal")

    suspend fun getPayments(type: String): Result<Contracts> =
        client.getResult("$PREFIX-payments") {
            param("type", type)
        }

    suspend fun getPaymentsTypes(): Result<List<PaymentType>> =
        client.getResult("$PREFIX-payments-types")

    companion object {
        private const val PREFIX = "account"
    }
}
