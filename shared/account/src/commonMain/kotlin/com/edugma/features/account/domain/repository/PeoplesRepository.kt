package com.edugma.features.account.domain.repository

import com.edugma.core.api.model.PagingDto
import com.edugma.features.account.domain.model.peoples.Person

interface PeoplesRepository {
    suspend fun getPeople(
        url: String,
        query: String = "",
        page: String?,
        limit: Int = 100,
    ): PagingDto<Person>
}
