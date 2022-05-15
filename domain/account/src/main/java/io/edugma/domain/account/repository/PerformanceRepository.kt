package io.edugma.domain.account.repository

import io.edugma.domain.account.model.Performance
import kotlinx.coroutines.flow.Flow

interface PerformanceRepository {
    fun getCourses(): Flow<Result<List<Int>>>
    fun getSemesters(): Flow<Result<List<Int>>>
    fun getMarksBySemester(semester: Int? = null): Flow<Result<List<Performance>>>
}