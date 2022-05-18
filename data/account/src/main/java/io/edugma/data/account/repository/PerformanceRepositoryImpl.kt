package io.edugma.data.account.repository

import io.edugma.data.account.api.AccountService
import io.edugma.data.base.local.PreferencesDS
import io.edugma.data.base.local.getJsonLazy
import io.edugma.data.base.local.setJsonLazy
import io.edugma.domain.account.model.Performance
import io.edugma.domain.account.model.Personal
import io.edugma.domain.account.model.SemestersWithCourse
import io.edugma.domain.account.repository.PerformanceRepository
import io.edugma.domain.base.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PerformanceRepositoryImpl(
    private val api: AccountService,
    private val localStore: PreferencesDS
): PerformanceRepository {

    companion object {
        private const val PerformanceKey = "Performance"
        private const val CourseKey = "Courses"
        private const val SemesterKey = "Semesters"
    }
    override fun getCourses() =
        api.getCourses()
            .onSuccess(::setLocalCourses)
            .flowOn(Dispatchers.IO)

    override fun getSemesters() =
        api.getSemesters()
            .onSuccess(::setLocalCourses)
            .flowOn(Dispatchers.IO)

    override fun getCoursesWithSemesters() =
        api.getCoursesWithSemesters()
            .onSuccess{
                setLocalSemesters(it.coursesWithSemesters.keys.toList())
                setLocalCourses(it.coursesWithSemesters.values.toSet().toList())
            }
            .flowOn(Dispatchers.IO)

    override fun getMarksBySemester(semester: Int?) =
        api.getMarks(semester.toString())
            .onSuccess {
                if (semester == null) {
                    setLocalMarks(it)
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun getLocalMarks() = localStore.getJsonLazy<List<Performance>>(PerformanceKey)

    override suspend fun getLocalSemesters() = localStore.getJsonLazy<List<Int>>(SemesterKey)

    override suspend fun getLocalCourses() = localStore.getJsonLazy<List<Int>>(CourseKey)

    override suspend fun setLocalMarks(data: List<Performance>) {
        localStore.setJsonLazy(data, PerformanceKey)
    }

    override suspend fun setLocalSemesters(data: List<Int>) {
        localStore.setJsonLazy(data, SemesterKey)
    }

    override suspend fun setLocalCourses(data: List<Int>) {
        localStore.setJsonLazy(data, CourseKey)
    }

    override fun getCoursesWithSemestersLocal(): Flow<Pair<List<Int>, List<Int>>?> =
        flow {
            getLocalCourses()?.let { courses ->
                getLocalSemesters()?.let { semester ->
                    emit(courses to semester )
                }
            } ?: emit(null)
        }

    override fun getMarksLocal(): Flow<List<Performance>?> =
        flow {
            emit(getLocalMarks())
    }


}