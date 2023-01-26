package io.edugma.features.account.teachers

import androidx.paging.PagingSource
import androidx.paging.PagingState
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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Teacher> {
        val page = params.key ?: 1
        return try {
            teachersRepository.getTeachers(name, page, pageSize).let {
                val nextPage = when {
                    it.data.isEmpty() -> null
                    it.data.size < pageSize -> null
                    else -> it.nextPage
                }
                LoadResult.Page(
                    data = it.data,
                    prevKey = it.previousPage,
                    nextKey = nextPage,
                )
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }
}
