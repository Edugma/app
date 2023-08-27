package io.edugma.features.schedule.domain.model.source

enum class ScheduleSourcesTabs {
    Favorite,
    Group,
    Teacher,
    Student,
    Place,
    Subject,
    Complex,
    ;

    fun toSourceType(): ScheduleSources? {
        return when (this) {
            Favorite -> null
            Group -> ScheduleSources.Group
            Teacher -> ScheduleSources.Teacher
            Student -> ScheduleSources.Student
            Place -> ScheduleSources.Place
            Subject -> ScheduleSources.Subject
            Complex -> ScheduleSources.Complex
        }
    }
    companion object {
        // TODO fix Complex
        fun getAllTabs() = listOf(
            Favorite,
            Group,
            Teacher,
            Student,
            Place,
            Subject,
        )
    }
}
