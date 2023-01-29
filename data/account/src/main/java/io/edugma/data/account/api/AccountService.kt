package io.edugma.data.account.api

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
import io.edugma.domain.base.model.PagingDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountService {

    @POST("/login")
    fun login(@Body login: Login): Flow<Result<Token>>

    @GET("/peoples/classmates/")
    fun getClassmates(): Flow<Result<List<Student>>>

    @GET("/peoples/students/{pageSize}/{page}/{name}")
    suspend fun getStudents(
        @Path("name") name: String?,
        @Path("page") page: Int,
        @Path("pageSize") pageSize: Int,
    ): PagingDTO<Student>

    @GET("/peoples/teachers/{pageSize}/{page}/{name}")
    suspend fun getTeachers(
        @Path("name") name: String?,
        @Path("page") page: Int,
        @Path("pageSize") pageSize: Int,
    ): PagingDTO<Teacher>

    @GET("/applications")
    fun getApplications(): Flow<Result<List<Application>>>

    @GET("/performance/semesters")
    fun getSemesters(): Flow<Result<List<Int>>>

    @GET("/performance/courses_semesters")
    fun getCoursesWithSemesters(): Flow<Result<SemestersWithCourse>>

    @GET("/performance/semesters/{semester}")
    fun getMarks(
        @Path("semester") semester: String,
    ): Flow<Result<List<Performance>>>

    @GET("/performance/courses")
    fun getCourses(): Flow<Result<List<Int>>>

    @GET("/personal")
    fun getPersonalInfo(): Flow<Result<Personal>>

    @GET("/payments/{type}")
    fun getPayments(@Path("type") type: String): Flow<Result<Contracts>>

    @GET("/payments/types/")
    fun getPaymentsTypes(): Flow<Result<List<PaymentType>>>
}
