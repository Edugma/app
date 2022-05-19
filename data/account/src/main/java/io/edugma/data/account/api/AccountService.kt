package io.edugma.data.account.api

import io.edugma.domain.account.model.*
import io.edugma.domain.base.model.PagingDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface AccountService {

    @POST("/login")
    fun login(@Body login: Login): Flow<Result<Token>>

    @GET("/peoples/classmates")
    fun getClassmates(): Flow<Result<List<Student>>>

    @GET("/peoples/students/{name}")
    fun getStudents(
        @Path("name") name: String?, @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Flow<Result<PagingDTO<Student>>>

    @GET("/peoples/teachers/{name}")
    fun getTeachers(
        @Path("name") name: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Flow<Result<PagingDTO<Teacher>>>

    @GET("/applications")
    fun getApplications(): Flow<Result<List<Application>>>

    @GET("/performance/semesters")
    fun getSemesters(): Flow<Result<List<Int>>>

    @GET("/performance/courses_semesters")
    fun getCoursesWithSemesters(): Flow<Result<SemestersWithCourse>>

    @GET("/performance/semesters/{semester}")
    fun getMarks(
        @Path("semester") semester: String
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