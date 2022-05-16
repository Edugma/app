package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.domain.account.model.SemestersWithCourse
import io.edugma.domain.account.repository.PerformanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PerformanceRepositoryImpl(
    private val api: AccountService
): PerformanceRepository {
    override fun getCourses() =
        api.getCourses()
            .flowOn(Dispatchers.IO)

    override fun getSemesters() =
        api.getSemesters()
            .flowOn(Dispatchers.IO)

    override fun getCoursesWithSemesters() = api.getCoursesWithSemesters()

    override fun getMarksBySemester(semester: Int?) =
        api.getMarks(semester.toString())
            .flowOn(Dispatchers.IO)


}