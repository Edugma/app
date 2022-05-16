package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.model.SemestersWithCourse
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {
    fun getCourses(): Flow<Result<List<Int>>>
    fun getSemesters(): Flow<Result<List<Int>>>
    fun getCoursesWithSemesters(): Flow<Result<SemestersWithCourse>>
    fun getMarksBySemester(semester: Int? = null): Flow<Result<List<Performance>>>
}