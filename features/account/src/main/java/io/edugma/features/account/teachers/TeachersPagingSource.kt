package io.edugma.features.account.teachers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository
import kotlinx.coroutines.flow.collect

class TeachersPagingSource(
    private val teachersRepository: PeoplesRepository,
    private val name: String,
    private val pageSize: Int
    ) : PagingSource<Int, Teacher>() {

    override fun getRefreshKey(state: PagingState<Int, Teacher>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Teacher> {
        val page = params.key ?: 1
        return try {
            teachersRepository.getTeachers(name, page, pageSize).let {
                LoadResult.Page(
                    data = it.data,
                    prevKey = it.previousPage,
                    nextKey = it.nextPage
                )
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

}