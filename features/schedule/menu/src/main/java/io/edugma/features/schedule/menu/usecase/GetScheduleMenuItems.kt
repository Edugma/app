package io.edugma.features.schedule.menu.usecase

import io.edugma.features.schedule.domain.usecase.IsScheduleSourceSelectedUseCase
import io.edugma.features.schedule.menu.model.MenuItem

class GetScheduleMenuItems(
    private val isScheduleSourceSelectedUseCase: IsScheduleSourceSelectedUseCase,
) {
    suspend operator fun invoke(): List<List<MenuItem>> {
        return if (isScheduleSourceSelectedUseCase()) {
            listOf(
                listOf(
                    MenuItem.DailySchedule,
                    MenuItem.Calendar,
                ),
                listOf(
                    MenuItem.LessonsReview,
                    MenuItem.Empty.forItem(MenuItem.LessonsReview),
                    MenuItem.Empty.forItem(MenuItem.LessonsReview),
//                    MenuItem.ChaneHistory,
//                    MenuItem.AppWidget,
                ),
//                listOf(
//                    MenuItem.FindFreePlace,
//                    MenuItem.Empty.forItem(MenuItem.FindFreePlace),
//                ),
            )
        } else {
            listOf(
//                listOf(
//                    MenuItem.FindFreePlace,
//                    MenuItem.Empty.forItem(MenuItem.FindFreePlace),
//                ),
            )
        }
    }
}
