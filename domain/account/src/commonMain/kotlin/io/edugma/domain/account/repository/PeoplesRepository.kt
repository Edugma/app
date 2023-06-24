package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.base.model.PagingDTO
import kotlinx.coroutines.flow.Flow

interface PeoplesRepository {
    suspend fun getTeachers(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Teacher>
    suspend fun getStudents(name: String = "", page: Int = 1, pageSize: Int = 100): PagingDTO<Student>
    fun getClassmates(): Flow<Result<List<Student>>>
    suspend fun getClassmatesSuspend(): Result<List<Student>>
    suspend fun saveClassmates(students: List<Student>)
    suspend fun loadClassmates(): List<Student>?
}
