package io.edugma.features.account.marks

import io.edugma.core.arch.mvi.viewmodel.BaseViewModel
import io.edugma.core.utils.isNotNull
import io.edugma.core.utils.isNull
import io.edugma.core.utils.viewmodel.launchCoroutine
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.features.account.marks.Filter.Course
import io.edugma.features.account.marks.Filter.Name
import io.edugma.features.account.marks.Filter.Semester
import io.edugma.features.account.marks.Filter.Type
import kotlinx.coroutines.async

class PerformanceViewModel(private val repository: PerformanceRepository) :
    BaseViewModel<MarksState>(MarksState()) {

    init {
        loadMarks()
    }

    fun loadMarks() {
        launchCoroutine {
            setLoading(true)
            setError(false)

            val localMarks = async { repository.getLocalMarks() }
            val coursesAndSemesters = async { repository.getCoursesWithSemestersSuspend() }
            val marks = async { repository.getMarksBySemesterSuspend() }

            localMarks.await()?.let {
                setFilters(
                    courses = repository.getLocalCourses()?.toSet() ?: emptySet(),
                    semesters = repository.getLocalSemesters()?.toSet() ?: emptySet(),
                    types = it.getExamTypes(),
                )
                setPerformanceData(it)
            }

            kotlin.runCatching {
                coursesAndSemesters.await().getOrThrow() to marks.await().getOrThrow()
            }
                .onSuccess {
                    val semestersWithCourses = it.first.coursesWithSemesters
                    val performance = it.second
                    setPerformanceData(performance)
                    setFilters(
                        courses = semestersWithCourses.values.toSet(),
                        semesters = semestersWithCourses.keys,
                        types = performance.getExamTypes(),
                    )
                }
                .onFailure {
                    setError(true)
                }

            setLoading(false)
        }
    }

    fun openBottomSheetClick(performance: Performance?) {
        mutateState {
            state = state.copy(selectedPerformance = performance)
        }
    }

    private fun setPerformanceData(data: List<Performance>) {
        mutateState {
            state = state.copy(data = data)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        mutateState {
            state = state.copy(isLoading = isLoading)
        }
    }

    private fun setError(isError: Boolean) {
        mutateState {
            state = state.copy(isError = isError)
        }
    }

    private fun setFilters(
        courses: Set<Int>? = null,
        semesters: Set<Int>? = null,
        types: Set<String>? = null,
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
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter),
                    )
                    is Semester -> state.copy(
                        semesters = state.semesters.updateFilter(filter) as Set<Semester>,
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter),
                    )
                    is Type -> state.copy(
                        types = state.types.updateFilter(filter) as Set<Type>,
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter),
                    )
                    is Name -> state.copy(
                        name = filter.copy(isChecked = !filter.isChecked),
                        currentFilters = state.currentFilters.addOrDeleteFilter(filter),
                    )
                }
            }
        }
    }

    fun resetFilters() {
        mutateState {
            state = state.copy(
                currentFilters = emptySet(),
                courses = state.courses.map { it.copy(isChecked = false) }.toSet(),
                semesters = state.semesters.map { it.copy(isChecked = false) }.toSet(),
                types = state.types.map { it.copy(isChecked = false) }.toSet(),
            )
        }
    }

    private fun List<Performance>.getExamTypes() = map { it.examType }.toSet()

    // todo рефакторить и вынести в usecase
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
    val selectedPerformance: Performance? = null,
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
                if (filteredCourses.isNotEmpty()) {
                    if (!filteredCourses.contains(course)) return@filter false
                }
                if (filteredSemesters.isNotEmpty()) {
                    if (!filteredSemesters.contains(semester)) return@filter false
                }
                if (filteredTypes.isNotEmpty()) {
                    if (!filteredTypes.contains(type)) return@filter false
                }
                if (name.isChecked && !performance.name.contains(name.value, ignoreCase = true)) {
                    return@filter false
                }
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
        override val mappedValue: String = "$value курс",
    ) : Filter<Int>(value, isChecked)

    data class Semester(
        override val value: Int,
        override val isChecked: Boolean = false,
        override val mappedValue: String = "$value семестр",
    ) : Filter<Int>(value, isChecked)

    data class Type(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value,
    ) : Filter<String>(value, isChecked)

    data class Name(
        override val value: String,
        override val isChecked: Boolean = false,
        override val mappedValue: String = value,
    ) : Filter<String>(value, isChecked)
}
