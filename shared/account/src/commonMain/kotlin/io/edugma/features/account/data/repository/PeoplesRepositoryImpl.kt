package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.get
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.onSuccess
import io.edugma.data.base.consts.CacheConst.ClassmatesKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository
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
