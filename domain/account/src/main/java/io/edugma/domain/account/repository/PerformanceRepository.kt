package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Marks
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {
    fun getCourses(): Flow<Result<List<Int>>>
    fun getSemesters(): Flow<Result<List<Int>>>
    fun getMarksByCourse(course: Int): Flow<Result<List<Marks>>>
    fun getMarksBySemester(semester: Int): Flow<Result<Marks>>
    fun getMarks(): Flow<Result<List<Marks>>>
}