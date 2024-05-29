package com.edugma.features.schedule.domain.model.compact

import kotlinx.serialization.Serializable

@Serializable
data class CompactLessonSubjectInfo(
    val id: String,
    val title: String,
    val type: String?,
    val description: String?,
) : Comparable<CompactLessonSubjectInfo> {
    override fun compareTo(other: CompactLessonSubjectInfo): Int {
        return title.compareTo(other.title)
    }
}
