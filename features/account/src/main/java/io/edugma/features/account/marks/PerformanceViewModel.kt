package io.edugma.features.account.marks

import android.util.Log
import androidx.lifecycle.viewModelScope
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.base.utils.onFailure
import io.edugma.domain.base.utils.onSuccess
import io.edugma.features.account.marks.Filter.*
import io.edugma.features.base.core.mvi.BaseViewModel
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
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
            repository.getLocalMarks()?.let {
                delay(300) //todo говнокод пофиксить
                setFilters(
                    courses = repository.getLocalCourses()?.toSet() ?: emptySet(),
                    semesters = repository.getLocalSemesters()?.toSet() ?: emptySet(),
                    types = it.getExamTypes()
                )
                setPerformanceData(it)
            }
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
                    setFilters(
                        courses = semestersWithCourses.values.toSet(),
                        semesters = semestersWithCourses.keys,
                        types = performance.getExamTypes()
                    )
                    setLoading(false)
                }
                .onFailure {
                    Log.e("performance loading error", it.localizedMessage ?: it.message ?: it::class.java.canonicalName)
                    setError()
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
            state = state.copy(isLoading = isLoading, isError = !isLoading && state.isError)
        }
    }

    private fun setError() {
        mutateState {
            state = state.copy(isError = true, isLoading = false)
        }
    }

    private fun setFilters(
        courses: Set<Int>? = null,
        semesters: Set<Int>? = null,
        types: Set<String>? = null
    ) {
        mutateState {
            state = state.copy(
                courses = courses?.map { Course(it) }?.toSet() ?: state.courses,
                semesters = semesters?.map { Semester(it) }?.toSet() ?: state.semesters,
                types = types?.map { Type(it) }?.toSet() ?: state.types,
            )
        }
    }

    fun updateFilter(filter: Filter<*>) {
        mutateState {
            if (!state.isLoading) {
                state = when (filter) {
                    is Course -> state.copy(
                        courses = state.courses.updateFilter(filter) as Set<Course>,
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter)
                    )
                    is Semester -> state.copy(
                        semesters = state.semesters.updateFilter(filter) as Set<Semester>,
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter)
                    )
                    is Type -> state.copy(
                        types = state.types.updateFilter(filter) as Set<Type>,
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter)
                    )
                    is Name -> state.copy(
                        name = filter.copy(isChecked = !filter.isChecked),
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter)
                    )
                }
            }
        }
    }

    private fun List<Performance>.getExamTypes() = map { it.examType }.toSet()

    private fun<T> Set<Filter<T>>.updateFilter(newFilter: Filter<T>): Set<Filter<T>> {
        val newSet = toMutableList()
        newSet.forEachIndexed { index, filter ->
            if (filter == newFilter) {
                newSet[index] = when (newFilter) {
                    is Course -> (newFilter as Course).copy(isChecked = !newFilter.isChecked)
                    is Semester -> (newFilter as Semester).copy(isChecked = !newFilter.isChecked)
                    is Type -> (newFilter as Type).copy(isChecked = !newFilter.isChecked)
                    is Name -> (newFilter as Name).copy(isChecked = !newFilter.isChecked)
                } as Filter<T>
            }
        }
        return newSet.toSet()
    }

    private fun<T> Set<Filter<T>>.addOrDeleteFilter(newFilter: Filter<T>): Set<Filter<T>> {
        val newSet = toMutableList()
        if (newFilter.isChecked) {
            if (newFilter is Name) newSet.removeIf { it is Name }
            newSet.remove(newFilter)
        } else {
            val filter = when (newFilter) {
                is Course -> (newFilter as Course).copy(isChecked = !newFilter.isChecked)
                is Semester -> (newFilter as Semester).copy(isChecked = !newFilter.isChecked)
                is Type -> (newFilter as Type).copy(isChecked = !newFilter.isChecked)
                is Name -> {
                    newSet.removeIf { it is Name }
                    (newFilter as Name).copy(isChecked = !newFilter.isChecked)
                }
            }
            newSet.add(filter as Filter<T>)
        }
        return newSet.toSet()
    }

}

data class MarksState(
    val data: List<Performance>? = null,
    val courses: Set<Course> = emptySet(),
    val semesters: Set<Semester> = emptySet(),
    val types: Set<Type> = emptySet(),
    val name: Name = Name(""),
    val currentFilters: Set<Filter<*>> = emptySet(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    val placeholders = data.isNull() && isLoading && !isError
    val bottomSheetPlaceholders = (isLoading && !isError) || (isError && data.isNull())
    val isRefreshing = data.isNotNull() && isLoading && !isError

    private val filteredCourses = courses.filter { it.isChecked }.toSet()
    private val filteredSemesters = semesters.filter { it.isChecked }.toSet()
    private val filteredTypes = types.filter { it.isChecked }.toSet()

    private val enabledFilters = (filteredCourses + filteredSemesters + filteredTypes).let {
        if (name.isChecked) it.plus(name) else it
    }

    val filteredData = data?.filter { performance ->
        when {
            enabledFilters.isEmpty() -> true
            else -> {
                val course = Course(performance.course, true)
                val semester = Semester(performance.semester, true)
                val type = Type(performance.examType, true)
                if (filteredCourses.isNotEmpty())
                    if (!filteredCourses.contains(course)) return@filter false
                if (filteredSemesters.isNotEmpty())
                    if (!filteredSemesters.contains(semester)) return@filter false
                if (filteredTypes.isNotEmpty())
                    if (!filteredTypes.contains(type)) return@filter false
                if (name.isChecked && !performance.name.contains(name.value, ignoreCase = true))
                    return@filter false
                true
            }
        }
    }
}

sealed class Filter<out T>(open val value: T, open val isChecked: Boolean) {

    abstract val mappedValue: String

    data class Course(
        override val value: Int,
        override val isChecked: Boolean = false,
        override val mappedValue: String = "$value курс"
    ) : Filter<Int>(value, isChecked)

    data class Semester(
        override val value: Int,
        override val isChecked: Boolean = false,
        override val mappedValue: String = "$value семестр"
    ) : Filter<Int>(value, isChecked)

    data class Type(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value
    ) : Filter<String>(value, isChecked)

    data class Name(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value
    ) : Filter<String>(value, isChecked)

}