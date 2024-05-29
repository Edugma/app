package com.edugma.features.schedule.elements.lesson

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.features.schedule.domain.model.compact.zonedTime
import com.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import com.edugma.features.schedule.domain.model.lesson.LessonEvent

fun LazyListScope.LessonPlace(
    lessonEvent: LessonEvent,
    lessonDisplaySettings: LessonDisplaySettings,
    onLessonClick: (LessonEvent) -> Unit,
) {
    item {
        LessonTimeContent(lessonEvent)
    }
    item {
        LessonContent(
            lesson = lessonEvent,
            displaySettings = lessonDisplaySettings,
            onLessonClick = { onLessonClick(lessonEvent) },
        )
    }
}

@Composable
private fun LessonTimeContent(lessonEvent: LessonEvent) {

    val timeText = remember(lessonEvent.start, lessonEvent.end) {
        val startTime = lessonEvent.start.zonedTime()
        val endTime = lessonEvent.end.zonedTime()
        "$startTime - $endTime"
    }
    Text(
        text = timeText,
        style = EdTheme.typography.titleSmall,
        modifier = Modifier
            .padding(start = 34.dp, end = 34.dp, top = 4.dp, bottom = 2.dp),
    )
}
