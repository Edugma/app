package io.edugma.features.account.data.repository

import io.edugma.core.api.model.PagingDto
import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getData
import io.edugma.core.api.repository.save
import io.edugma.data.base.consts.CacheConst.ClassmatesKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.Teacher
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository

class PeoplesRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PeoplesRepository {

    override suspend fun getTeachers(
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<Teacher> = api.getTeachers(query, page, limit)

    override suspend fun getStudents(
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<Student> = api.getStudents(query, page, limit)

    override suspend fun getClassmates(): Result<List<Student>> {
        return api.getClassmates()
            .onSuccess { saveClassmates(it) }
    }

    override suspend fun saveClassmates(students: List<Student>) {
        cacheRepository.save(ClassmatesKey, students)
    }

    override suspend fun loadClassmates(): List<Student>? {
        return cacheRepository.getData(ClassmatesKey)
    }
}
