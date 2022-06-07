package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.domain.account.repository.PeoplesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PeoplesRepositoryImpl(
    private val api: AccountService
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

}