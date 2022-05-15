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
            repository.getCourses()
                .zip(repository.getSemesters()) { courses, semesters ->
                    runCatching {
                        courses.getOrThrow() to semesters.getOrThrow()
                    }
                }
                .zip(repository.getMarksBySemester()) { coursesWithSemester, performance ->
                    runCatching {
                        coursesWithSemester.getOrThrow() to performance.getOrThrow()
                    }
                }
                .onStart { setLoading(true) }
                .onSuccess {
                    val coursesWithSemester = it.first
                    val courses = coursesWithSemester.first
                    val semesters = coursesWithSemester.second
                    val performance = it.second
                    val semestersInCourse = ceil(semesters.count().toDouble() / (courses.lastOrNull() ?: 1 ).toDouble()).roundToInt()
                    val coursesAndSemesters = mutableMapOf<Int, Int>().apply {
                        courses.forEachIndexed { index, course ->
                            semesters
                                .subList(index * semestersInCourse, index * semestersInCourse + semestersInCourse)
                                .forEach { semester ->
                                    put(course, semester)
                                }
                        }
                    }
                    setPerformanceData(performance)
                    setCoursesSemestersData(coursesAndSemesters)
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
            state = state.copy(data = data, isLoading = false, isError = false)
        }
    }

    fun setCoursesSemestersData(data: Map<Int, Int>) {
        mutateState {
            state = state.copy(isLoading = false, isError = false, coursesAndSemesters = data)
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
            state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError)
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
    val isError: Boolean = false
)