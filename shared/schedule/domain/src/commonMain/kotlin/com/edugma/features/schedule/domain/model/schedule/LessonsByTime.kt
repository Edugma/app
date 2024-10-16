package com.edugma.features.schedule.domain.model.schedule

import com.edugma.features.schedule.domain.model.lesson.LessonEvent
import com.edugma.features.schedule.domain.model.lesson.LessonTime
import kotlinx.serialization.Serializable

@Serializable
data class LessonsByTime(
    val time: LessonTime,
    val lessons: List<LessonEvent>,
)
