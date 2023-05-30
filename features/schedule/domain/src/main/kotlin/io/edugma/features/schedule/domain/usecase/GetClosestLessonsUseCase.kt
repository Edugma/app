package io.edugma.features.schedule.domain.usecase

import io.edugma.domain.base.utils.TimeZones
import io.edugma.domain.base.utils.nowLocalTime
import io.edugma.domain.base.utils.plus
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetClosestLessonsUseCase {
    operator fun invoke(
        lessons: List<LessonsByTime>,
        timeToCombine: Duration = 15.toDuration(DurationUnit.MINUTES),
    ): List<LessonsByTime> {
        val now = Clock.System.nowLocalTime(TimeZones.main)
        val latestTimeToCombine = now.plus(timeToCombine)

        var firstCeilingLessonIndex: Int = -1
        val closestLessons = mutableListOf<Int>()

        lessons.forEachIndexed { lessonToAddIndex, lessonToAdd ->
            if (now in lessonToAdd.time) {
                closestLessons.add(lessonToAddIndex)
            } else if (now < lessonToAdd.time.start) {
                if (firstCeilingLessonIndex == -1 ||
                    lessonToAdd.time.start < lessons[firstCeilingLessonIndex].time.start
                ) {
                    firstCeilingLessonIndex = lessonToAddIndex
                }

                if (lessonToAdd.time.start <= latestTimeToCombine) {
                    closestLessons.add(lessonToAddIndex)
                }
            }
        }

        if (firstCeilingLessonIndex != -1 && firstCeilingLessonIndex !in closestLessons) {
            closestLessons.add(firstCeilingLessonIndex)
        }

        return closestLessons.map { lessons[it] }.sortedBy { it.time }
    }
}
