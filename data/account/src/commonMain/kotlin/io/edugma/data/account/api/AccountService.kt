package io.edugma.data.account.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.edugma.core.api.model.PagingDTO
import io.edugma.domain.account.model.Application
import io.edugma.domain.account.model.Contracts
import io.edugma.domain.account.model.Login
import io.edugma.domain.account.model.PaymentType
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.model.SemestersWithCourse
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.Token
import io.edugma.domain.account.model.student.Student

interface AccountService {

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body login: Login): Result<Token>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun loginSuspend(@Body login: Login): Result<Token>

    @GET("peoples/classmates/")
    suspend fun getClassmates(): Result<List<Student>>

    @GET("peoples/classmates/")
    suspend fun getClassmatesSuspend(): Result<List<Student>>

    @GET("peoples/students/{pageSize}/{page}/{name}")
    suspend fun getStudents(
        @Path("name") name: String,
        @Path("page") page: Int,
        @Path("pageSize") pageSize: Int,
    ): Result<PagingDTO<Student>>

    @GET("peoples/teachers/{pageSize}/{page}/{name}")
    suspend fun getTeachers(
        @Path("name") name: String,
        @Path("page") page: Int,
        @Path("pageSize") pageSize: Int,
    ): Result<PagingDTO<Teacher>>

    @GET("applications")
    suspend fun getApplications(): Result<List<Application>>

    @GET("performance/semesters")
    suspend fun getSemesters(): Result<List<Int>>

    @GET("performance/courses_semesters")
    suspend fun getCoursesWithSemesters(): Result<SemestersWithCourse>

    @GET("performance/courses_semesters")
    suspend fun getCoursesWithSemestersSuspend(): Result<SemestersWithCourse>

    @GET("performance/semesters/{semester}")
    suspend fun getMarks(
        @Path("semester") semester: String,
    ): Result<List<Performance>>

    @GET("performance/semesters/{semester}")
    suspend fun getMarksSuspend(
        @Path("semester") semester: String,
    ): Result<List<Performance>>

    @GET("performance/courses")
    suspend fun getCourses(): Result<List<Int>>

    @GET("personal")
    suspend fun getPersonalInfo(): Result<Personal>

    @GET("personal")
    suspend fun getPersonalInfoSuspend(): Result<Personal>

    @GET("payments/{type}")
    suspend fun getPayments(@Path("type") type: String): Result<Contracts>

    @GET("payments/{type}")
    suspend fun getPaymentsSuspend(@Path("type") type: String): Result<Contracts>

    @GET("payments/types/")
    suspend fun getPaymentsTypes(): Result<List<PaymentType>>

    @GET("getLkToken")
    suspend fun getLkToken(): Result<Token>
}
