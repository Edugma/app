package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.consts.CacheConst.ClassmatesKey
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getJsonLazy
import io.edugma.data.base.local.setJsonLazy
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.repository.PeoplesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PeoplesRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS
): PeoplesRepository {
    override suspend fun getTeachers(
        name: String,
        page: Int,
        pageSize: Int
    ) = api.getTeachers(name, page, pageSize)

    override suspend fun getStudents(
        name: String,
        page: Int,
        pageSize: Int
    ) = api.getStudents(name, page, pageSize)

    override fun getClassmates() =
        api.getClassmates()
            .flowOn(Dispatchers.IO)

    override suspend fun saveClassmates(students: List<Student>) {
        localStore.setJsonLazy(students, ClassmatesKey)
    }

    override suspend fun loadClassmates(): List<Student>? {
        return localStore.getJsonLazy(ClassmatesKey)
    }

}