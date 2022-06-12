package io.edugma.features.account.students

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.edugma.domain.account.model.student.Student
import io.edugma.domain.account.model.Teacher
import io.edugma.domain.account.repository.PeoplesRepository
import kotlinx.coroutines.flow.collect

class StudentsPagingSource(
    private val studentsRepository: PeoplesRepository,
    private val name: String,
    private val pageSize: Int
    ) : PagingSource<Int, Student>() {

    override fun getRefreshKey(state: PagingState<Int, Student>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Student> {
        val page = params.key ?: 1
        return try {
            studentsRepository.getStudents(name, page, pageSize).let {
                val nextPage = when {
                    it.data.isEmpty() -> null
                    it.data.size < pageSize -> null
                    else -> it.nextPage
                }
                LoadResult.Page(
                    data = it.data,
                    prevKey = it.previousPage,
                    nextKey = nextPage
                )
            }
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

}