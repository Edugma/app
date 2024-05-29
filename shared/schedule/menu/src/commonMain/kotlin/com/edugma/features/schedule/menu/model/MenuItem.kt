package com.edugma.features.schedule.menu.model

sealed class MenuItem(
    val weight: Float,
) {
    object DailySchedule : MenuItem(
        weight = 2f,
    )
    object Calendar : MenuItem(
        weight = 1f,
    )
    object LessonsReview : MenuItem(
        weight = 1f,
    )
    object ChaneHistory : MenuItem(
        weight = 1f,
    )
    object AppWidget : MenuItem(
        weight = 1f,
    )
    object FindFreePlace : MenuItem(
        weight = 1.5f,
    )
    class Empty(
        weight: Float,
    ) : MenuItem(weight = weight) {
        companion object {
            fun forItem(item: MenuItem) = Empty(item.weight)
        }
    }
}
