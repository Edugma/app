package io.edugma.features.account.domain.repository

import io.edugma.features.account.domain.model.Performance
import io.edugma.features.account.domain.model.SemestersWithCourse
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {
    fun getCourses(): Flow<Result<List<Int>>>
    fun getSemesters(): Flow<Result<List<Int>>>
    suspend fun getCoursesWithSemesters(): Result<SemestersWithCourse>
    suspend fun getMarksBySemester(semester: Int? = null): Result<List<Performance>>
    suspend fun getLocalMarks(): List<Performance>?
    suspend fun getLocalSemesters(): List<Int>?
    suspend fun getLocalCourses(): List<Int>?
    suspend fun setLocalMarks(data: List<Performance>)
    suspend fun setLocalSemesters(data: List<Int>)
    suspend fun setLocalCourses(data: List<Int>)
    fun getCoursesWithSemestersLocal(): Flow<Pair<List<Int>, List<Int>>?>
    fun getMarksLocal(): Flow<List<Performance>?>
}
