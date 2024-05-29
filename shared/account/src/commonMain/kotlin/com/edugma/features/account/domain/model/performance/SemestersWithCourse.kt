package com.edugma.features.account.domain.model.performance

import kotlinx.serialization.Serializable

@Serializable
data class SemestersWithCourse(
    val coursesWithSemesters: Map<Int, Int>,
)
