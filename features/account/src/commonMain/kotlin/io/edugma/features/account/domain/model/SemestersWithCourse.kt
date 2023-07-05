package io.edugma.features.account.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SemestersWithCourse(
    val coursesWithSemesters: Map<Int, Int>,
)
