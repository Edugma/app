package com.edugma.features.account.data.repository

import com.edugma.core.api.model.PagingDto
import com.edugma.core.api.repository.CacheRepository
import com.edugma.features.account.data.api.AccountService
import com.edugma.features.account.domain.model.peoples.Person
import com.edugma.features.account.domain.repository.PeoplesRepository

class PeoplesRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PeoplesRepository {

    override suspend fun getPeople(
        url: String,
        query: String,
        page: String?,
        limit: Int,
    ): PagingDto<Person> = api.getPeople(
        url = url,
        query = query,
        page = page,
        limit = limit,
    )
}
