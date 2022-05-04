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

    @GET("/performance")
    fun getMarks(): Flow<Result<List<Marks>>>

    @GET("/performance/semesters")
    fun getSemesters(): Flow<Result<List<Int>>>

    @GET("/performance/semesters/{semester}")
    fun getMarksBySemester(
        @Path("semester") semester: String
    ): Flow<Result<Marks>>

    @GET("/performance/courses")
    fun getCourses(): Flow<Result<List<Int>>>

    @GET("/performance/courses/{course}")
    fun getMarksByCourse(
        @Path("course") course: String
    ): Flow<Result<List<Marks>>>

    @GET("/personal2")
    fun getPersonalInfo(): Flow<Result<Personal>>

    @GET("/personal/orders")
    fun getOrders(): Flow<Result<List<Order>>>

    @GET("/payments")
    fun getPayments(): Flow<Result<List<Payments>>>

    @GET("/payments/types")
    fun getPaymentsTypes(): Flow<Result<List<PaymentType>>>

    @GET("/payment/{type}")
    fun getPayment(
        @Path("type") type: String
    ): Flow<Result<Payments>>

}