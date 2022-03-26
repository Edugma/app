package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Student
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.base.model.PagingDTO
import kotlinx.coroutines.flow.Flow

interface PeoplesRepository {
    fun getTeachers(name: String = "", page: Int = 1, pageSize: Int = 100): Flow<Result<PagingDTO<Teacher>>>
    fun getStudents(name: String = "", page: Int = 1, pageSize: Int = 100): Flow<Result<PagingDTO<Student>>>
    fun getClassmates(): Flow<Result<List<Student>>>
}