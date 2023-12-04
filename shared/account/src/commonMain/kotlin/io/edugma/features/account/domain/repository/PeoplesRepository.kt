package io.edugma.features.account.domain.repository

import io.edugma.core.api.model.PagingDto
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.model.student.Student

interface PeoplesRepository {
    suspend fun getTeachers(query: String = "", page: String?, limit: Int = 100): PagingDto<Teacher>
    suspend fun getStudents(query: String = "", page: String?, limit: Int = 100): PagingDto<Student>
    suspend fun getClassmates(): Result<List<Student>>
    suspend fun saveClassmates(students: List<Student>)
    suspend fun loadClassmates(): List<Student>?
}
