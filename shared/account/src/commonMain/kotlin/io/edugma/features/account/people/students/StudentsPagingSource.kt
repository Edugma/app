package io.edugma.features.account.people.students

import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import io.edugma.features.account.domain.model.student.Student
import io.edugma.features.account.domain.repository.PeoplesRepository

class StudentsPagingSource(
    private val studentsRepository: PeoplesRepository,
    private val name: String,
    private val pageSize: Int,
) : PagingSource<Int, Student>() {

    override fun getRefreshKey(state: PagingState<Int, Student>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, Student> {
        val page = params.key ?: 1
        return try {
            studentsRepository.getStudents(name, page, pageSize).let {
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
            PagingSourceLoadResultError(e)
        }
    }
}
