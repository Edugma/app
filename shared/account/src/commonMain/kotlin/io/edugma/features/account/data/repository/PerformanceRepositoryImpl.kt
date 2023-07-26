package io.edugma.features.account.data.repository

import io.edugma.core.api.repository.CacheRepository
import io.edugma.core.api.repository.get
import io.edugma.core.api.repository.save
import io.edugma.core.api.utils.onSuccess
import io.edugma.data.base.consts.CacheConst.CourseKey
import io.edugma.data.base.consts.CacheConst.PerformanceKey
import io.edugma.data.base.consts.CacheConst.SemesterKey
import io.edugma.features.account.data.api.AccountService
import io.edugma.features.account.domain.model.Performance
import io.edugma.features.account.domain.model.SemestersWithCourse
import io.edugma.features.account.domain.repository.PerformanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class PerformanceRepositoryImpl(
    private val api: AccountService,
    private val cacheRepository: CacheRepository,
) : PerformanceRepository {

    override fun getCourses() =
        flow { emit(api.getCourses()) }
            .onSuccess(::setLocalCourses)
            .flowOn(Dispatchers.IO)

    override fun getSemesters() =
        flow { emit(api.getSemesters()) }
            .onSuccess(::setLocalCourses)
            .flowOn(Dispatchers.IO)

    override fun getCoursesWithSemesters() =
        flow { emit(api.getCoursesWithSemesters()) }
            .onSuccess {
                setLocalSemesters(it.coursesWithSemesters.keys.toList())
                setLocalCourses(it.coursesWithSemesters.values.toSet().toList())
            }
            .flowOn(Dispatchers.IO)

    override suspend fun getCoursesWithSemestersSuspend(): Result<SemestersWithCourse> {
        return api.getCoursesWithSemestersSuspend()
            .onSuccess {
                setLocalSemesters(it.coursesWithSemesters.keys.toList())
                setLocalCourses(it.coursesWithSemesters.values.toSet().toList())
            }
    }

    override fun getMarksBySemester(semester: Int?) =
        flow { emit(api.getMarks(semester?.toString().orEmpty())) }
            .onSuccess {
                if (semester == null) {
                    setLocalMarks(it)
                }
            }
            .flowOn(Dispatchers.IO)

    override suspend fun getMarksBySemesterSuspend(semester: Int?): Result<List<Performance>> {
        return api.getMarksSuspend(semester?.toString().orEmpty())
            .onSuccess {
                withContext(Dispatchers.IO) {
                    if (semester == null) {
                        setLocalMarks(it)
                    }
                }
            }
    }

    override suspend fun getLocalMarks(): List<Performance>? {
        return cacheRepository.get<List<Performance>>(PerformanceKey)
    }

    override suspend fun getLocalSemesters(): List<Int>? {
        return cacheRepository.get<List<Int>>(SemesterKey)
    }

    override suspend fun getLocalCourses(): List<Int>? {
        return cacheRepository.get<List<Int>>(CourseKey)
    }

    override suspend fun setLocalMarks(data: List<Performance>) {
        cacheRepository.save(PerformanceKey, data)
    }

    override suspend fun setLocalSemesters(data: List<Int>) {
        cacheRepository.save(SemesterKey, data)
    }

    override suspend fun setLocalCourses(data: List<Int>) {
        cacheRepository.save(CourseKey, data)
    }

    override fun getCoursesWithSemestersLocal(): Flow<Pair<List<Int>, List<Int>>?> =
        flow {
            getLocalCourses()?.let { courses ->
                getLocalSemesters()?.let { semester ->
                    emit(courses to semester)
                }
            } ?: emit(null)
        }

    override fun getMarksLocal(): Flow<List<Performance>?> =
        flow {
            emit(getLocalMarks())
        }
}
