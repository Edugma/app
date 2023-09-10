package io.edugma.features.account.domain.repository

import io.edugma.core.api.model.PagingDTO
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.model.student.Student
import kotlinx.coroutines.flow.Flow

interface PeoplesRepository {
    suspend fun getTeachers(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Teacher>
    suspend fun getStudents(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Student>
    suspend fun getTeachersResult(name: String = "", page: Int = 1, pageSize: Int = 100): Result<PagingDTO<Teacher>>
    suspend fun getStudentsResult(name: String = "", page: Int = 1, pageSize: Int = 100): Result<PagingDTO<Student>>
    fun getClassmates(): Flow<Result<List<Student>>>
    suspend fun getClassmatesSuspend(): Result<List<Student>>
    suspend fun saveClassmates(students: List<Student>)
    suspend fun loadClassmates(): List<Student>?
}
