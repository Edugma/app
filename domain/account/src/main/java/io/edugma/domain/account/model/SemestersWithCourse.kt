package io.edugma.domain.account.model

import kotlinx.serialization.Serializable

@Serializable
data class SemestersWithCourse(
    val coursesWithSemesters: Map<Int, Int>
)
