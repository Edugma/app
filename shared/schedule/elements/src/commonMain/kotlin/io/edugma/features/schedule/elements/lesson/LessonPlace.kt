package io.edugma.features.schedule.elements.lesson

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.utils.Typed2Listener
import io.edugma.features.schedule.domain.model.lesson.Lesson
import io.edugma.features.schedule.domain.model.lesson.LessonDisplaySettings
import io.edugma.features.schedule.domain.model.lesson.LessonTime
import io.edugma.features.schedule.domain.model.schedule.LessonsByTime

fun LazyListScope.LessonPlace(
    lessonsByTime: LessonsByTime,
    lessonDisplaySettings: LessonDisplaySettings,
    onLessonClick: Typed2Listener<Lesson, LessonTime>,
) {
    item {
        LessonTimeContent(lessonsByTime.time)
    }
    items(lessonsByTime.lessons) { lesson ->
        LessonContent(
            lesson = lesson,
            displaySettings = lessonDisplaySettings,
            onLessonClick = { onLessonClick(it, lessonsByTime.time) },
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
