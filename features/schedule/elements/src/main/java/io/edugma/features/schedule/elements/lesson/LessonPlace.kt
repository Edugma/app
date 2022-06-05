package io.edugma.features.schedule.elements.lesson

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.domain.schedule.model.lesson.Lesson
import io.edugma.domain.schedule.model.lesson.LessonDisplaySettings
import io.edugma.domain.schedule.model.lesson.LessonTime
import io.edugma.domain.schedule.model.schedule.LessonsByTime
import io.edugma.features.base.core.utils.Typed2Listener
import io.edugma.features.base.elements.placeholder

fun LazyListScope.LessonPlace(
    lessonsByTime: LessonsByTime,
    lessonDisplaySettings: LessonDisplaySettings,
    isLoading: Boolean = false,
    onLessonClick: Typed2Listener<Lesson, LessonTime>
) {
    item {
        LessonTimeContent(lessonsByTime.time, isLoading)
    }
    items(lessonsByTime.lessons) { lesson ->
        LessonContent(
            lesson = lesson,
            displaySettings = lessonDisplaySettings,
            isLoading = isLoading,
            onLessonClick = { onLessonClick(it, lessonsByTime.time) }
        )
    }
}

@Composable
private fun LessonTimeContent(lessonTime: LessonTime, isLoading: Boolean = false) {
    Text(
        text = "${lessonTime.start} - ${lessonTime.end}",
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .padding(start = 34.dp, end = 34.dp, top = 4.dp, bottom = 2.dp)
            .placeholder(visible = isLoading)
    )
}