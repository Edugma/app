package io.edugma.features.account.marks

import android.util.Log
import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.features.base.core.mvi.BaseMutator
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.roundToInt

class PerformanceViewModel(private val repository: PerformanceRepository) :
    BaseViewModel<MarksState>(MarksState()) {

    init {
        loadMarks()
    }

    fun loadMarks() {
        viewModelScope.launch {
            repository.getCoursesWithSemesters()
                .zip(repository.getMarksBySemester()) { coursesWithSemester, performance ->
                    runCatching {
                        coursesWithSemester.getOrThrow() to performance.getOrThrow()
                    }
                }
                .onStart { setLoading(true) }
                .onSuccess {
                    val semestersWithCourses = it.first.coursesWithSemesters
                    val performance = it.second
                    setPerformanceData(performance)
                    setCoursesSemestersData(semestersWithCourses)
                    setLoading(false)
                }
                .onFailure {
                    Log.e("performance loading error", it.localizedMessage ?: it.message ?: it::class.java.canonicalName)
                    setError(true)
                }
                .collect()
        }
    }

    fun loadSemesterMarks(semester: Int) {
        repository.getMarksBySemester(semester)
            .onStart { setLoading(true) }
            .onSuccess(::setPerformanceData)
            .onFailure { setError(true) }
    }

    private fun setPerformanceData(data: List<Performance>) {
        mutateState {
            state = state.copy(data = data)
        }
    }

    private fun setCoursesSemestersData(data: Map<Int, Int>) {
        mutateState {
            state = state.copy(isLoading = false)
        }
    }

//    fun setSemester(semester: Int) {
//        mutateState {
//            state = state.copy(
//                semester = semester,
//                filteredData = state.data.first { it.semester == semester })
//        }
//    }

    private fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError, placeholders = if (!isLoading) false else state.placeholders)
        }
    }

    private fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError, isLoading = false)
        }
    }

}

data class MarksState(
    val data: List<Performance> = emptyList(),
    val coursesAndSemesters: Map<Int, Int> = emptyMap(),
    val semester: Int? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val placeholders: Boolean = true,
)