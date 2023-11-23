package io.edugma.features.account.data.repository

import io.edugma.core.api.model.PagingDTO
import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.getData
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.onSuccess
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
        name: String,
        page: Int,
        pageSize: Int,
    ) = api.getTeachers(name, page, pageSize).getOrThrow()

    override suspend fun getStudents(
        name: String,
        page: Int,
        pageSize: Int,
    ) = api.getStudents(name, page, pageSize).getOrThrow()

    override suspend fun getTeachersResult(
        name: String,
        page: Int,
        pageSize: Int,
    ): Result<PagingDTO<Teacher>> = api.getTeachers(name, page, pageSize)

    override suspend fun getStudentsResult(
        name: String,
        page: Int,
        pageSize: Int,
    ): Result<PagingDTO<Student>> = api.getStudents(name, page, pageSize)

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
