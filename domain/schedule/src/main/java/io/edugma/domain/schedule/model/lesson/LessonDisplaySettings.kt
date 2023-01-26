package io.edugma.domain.schedule.model.lesson

data class LessonDisplaySettings(
    val showTeachers: Boolean,
    val showGroups: Boolean,
    val showPlaces: Boolean,
) {
    companion object {
        val Default = LessonDisplaySettings(
            showTeachers = true,
            showPlaces = true,
            showGroups = true,
        )
    }
}
