package io.edugma.features.account.marks

import android.util.Log
import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.base.core.mvi.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class PerformanceViewModel(private val repository: PerformanceRepository) :
    BaseViewModel<MarksState>(MarksState()) {

    init {
        loadMarks()
    }

    fun loadMarks() {
        viewModelScope.launch {
            setLoading(true)

            //todo говнокод
            //todo пофиксить фриз при  загрузке
            repository.getMarksLocal().zip(repository.getCoursesWithSemestersLocal()) { performance, coursesSemesters ->
                performance?.let {
                    setPerformanceData(performance)
                    changeAvailableFilters(
                        courses = coursesSemesters?.first,
                        semesters = coursesSemesters?.second,
                        type = performance.getExamTypes()
                    )
                    filterAndSetData(performance)
                    setLoading(false)
                }
            }.collect()

            repository.getCoursesWithSemesters()
                .zip(repository.getMarksBySemester()) { coursesWithSemester, performance ->
                    runCatching {
                        coursesWithSemester.getOrThrow() to performance.getOrThrow()
                    }
                }
                .onSuccess {
                    val semestersWithCourses = it.first.coursesWithSemesters
                    val performance = it.second
                    setPerformanceData(performance)
                    changeAvailableFilters(
                        semestersWithCourses.values.toSet().toList(),
                        semestersWithCourses.keys.toList(),
                        performance.getExamTypes()
                    )
                    filterAndSetData(performance)
                    setLoading(false)
                }
                .onFailure {
                    Log.e("performance loading error", it.localizedMessage ?: it.message ?: it::class.java.canonicalName)
                    setError(true)
                }
                .collect()
        }
    }

    private fun setPerformanceData(data: List<Performance>) {
        mutateState {
            state = state.copy(data = data)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError, placeholders = if (!isLoading) false else state.placeholders)
        }
    }

    private fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError, isLoading = false, placeholders = false)
        }
    }

    private fun changeAvailableFilters(
        courses: List<Int>? = null,
        semesters: List<Int>? = null,
        type: List<String>? = null
    ) {
        mutateState {
            state = state.copy(
                availableFilters = Filters(
                    courses ?: state.availableFilters.courses,
                    semesters ?: state.availableFilters.semesters,
                    type ?: state.availableFilters.types
                )
            )
        }
    }

    fun changeCurrentFilters(
        course: Int? = null,
        semester: Int? = null,
        type: String? = null
    ) {
        mutateState {
            state = state.copy(
                currentFilters = Filters(
                    courses = state.currentFilters.courses.removeOrAddIfContains(course),
                    semesters = state.currentFilters.semesters.removeOrAddIfContains(semester),
                    types = state.currentFilters.types.removeOrAddIfContains(type),
                )
            )
        }
    }

    private fun filterAndSetData(nonFilteredData: List<Performance>? = null) {
        mutateState {
            state = state.copy(
                filteredData = state.currentFilters.let { nonFilteredData?.filter(it) ?: state.data.filter(it) })
        }
    }

    private fun List<Performance>.getExamTypes() = map { it.examType }.toSet().toList()

    private fun List<Performance>.filter(filters: Filters): List<Performance> {
        if (filters.isEmpty()) return this
        return filter {
            filters.types.checkContainsIfNotEmpty(it.examType) &&
                    filters.semesters.checkContainsIfNotEmpty(it.semester) &&
                    filters.courses.checkContainsIfNotEmpty(it.course)
        }
    }

    private fun<T> List<T>.checkContainsIfNotEmpty(element: T) = if (isNotEmpty()) contains(element) else true

    private fun<T> List<T>.removeOrAddIfContains(element: T?) =
        toMutableList().apply { element?.let { if (!contains(it)) add(element) else remove(element) } }

}

data class MarksState(
    val data: List<Performance> = emptyList(),
    val filteredData: List<Performance> = emptyList(),
    val availableFilters: Filters = Filters(),
    val currentFilters: Filters = Filters(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val placeholders: Boolean = true,
)

data class Filters(
    val courses: List<Int> = emptyList(),
    val semesters: List<Int> = emptyList(),
    val types: List<String> = emptyList()
)

fun Filters.isEmpty() = courses.isEmpty() && semesters.isEmpty() && types.isEmpty()

fun MarksState.isCourseChecked(course: Int) = currentFilters.courses.contains(course)

fun MarksState.isSemesterChecked(semester: Int) = currentFilters.semesters.contains(semester)

fun MarksState.isTypeChecked(type: String) = currentFilters.types.contains(type)