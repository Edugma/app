package io.edugma.features.schedule.elements.lesson

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.utils.Typed2Listener
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonEvent
import io.edugma.features.schedule.domain.model.lesson.LessonTime

fun LazyListScope.LessonPlace(
    lessonEvent: LessonEvent,
    lessonDisplaySettings: LessonDisplaySettings,
    onLessonClick: Typed2Listener<LessonEvent, LessonTime>,
) {
    item {
        LessonTimeContent(lessonEvent)
    }
    item {
        LessonContent(
            lesson = lessonEvent,
            displaySettings = lessonDisplaySettings,
            onLessonClick = { onLessonClick(it, lessonEvent) },
        )
    }
}

@Composable
private fun LessonTimeContent(lessonTime: LessonTime) {
    Text(
        text = "${lessonTime.start} - ${lessonTime.end}",
        style = EdTheme.typography.titleSmall,
        modifier = Modifier
            .padding(start = 34.dp, end = 34.dp, top = 4.dp, bottom = 2.dp),
    )
}
