package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.ClassmatesKey
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.repository.PeoplesRepository
import io.edugma.domain.base.repository.CacheRepository
import io.edugma.domain.base.repository.get
import io.edugma.domain.base.repository.save
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

    override fun getClassmates() =
        flow { emit(api.getClassmates()) }
            .onSuccess { saveClassmates(it) }
            .flowOn(Dispatchers.IO)

    override suspend fun getClassmatesSuspend(): Result<List<Student>> {
        return api.getClassmatesSuspend()
            .onSuccess { saveClassmates(it) }
    }

    override suspend fun saveClassmates(students: List<Student>) {
        cacheRepository.save(ClassmatesKey, students)
    }

    override suspend fun loadClassmates(): List<Student>? {
        return cacheRepository.get(ClassmatesKey)
    }
}
