package com.edugma.features.schedule.menu.model

sealed class MenuItem(
    val weight: Float,
) {
    data object DailySchedule : MenuItem(
        weight = 2f,
    )
    data object Calendar : MenuItem(
        weight = 1f,
    )
    data object LessonsReview : MenuItem(
        weight = 1f,
    )
    data object ChaneHistory : MenuItem(
        weight = 1f,
    )
    data object AppWidget : MenuItem(
        weight = 1f,
    )
    data object FindFreePlace : MenuItem(
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
