package io.edugma.features.account.people.teachers

import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository

class TeachersPagingSource(
    private val teachersRepository: PeoplesRepository,
    private val name: String,
    private val pageSize: Int,
) : PagingSource<Int, Teacher>() {

    override fun getRefreshKey(state: PagingState<Int, Teacher>): Int? {
        return state.anchorPosition
    }
    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Teacher> {
        val page = params.key ?: 1
        return try {
            teachersRepository.getTeachers(name, page, pageSize).let {
                val nextPage = when {
                    it.data.isEmpty() -> null
                    it.data.size < pageSize -> null
                    else -> it.nextPage
                }
                PagingSourceLoadResultPage(
                    data = it.data,
                    prevKey = it.previousPage,
                    nextKey = nextPage,
                )
            }
        } catch (e: Throwable) {
            PagingSourceLoadResultError<Int, Teacher>(e)
        } as PagingSourceLoadResult<Int, Teacher>
    }
}
