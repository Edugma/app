package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.account.main.CurrentPerformance
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.WithContentAlpha

@Composable
fun PerformanceCard(
    performance: CurrentPerformance?,
    showCurrentPerformance: Boolean,
    enabled: Boolean,
    onClick: ClickListener,
) {
    EdCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(85.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled,
        onClick = onClick,
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Column {
                Text(text = "Успеваемость")
                Spacer(Modifier.height(4.dp))
                performance?.apply {
                    AnimatedText(performance, showCurrentPerformance)
                }
            }
        }
    }
}

@Composable
fun AnimatedText(performance: CurrentPerformance, isCurrent: Boolean) {
    val text = if (isCurrent) "За ${performance.lastSemesterNumber} семестр:" else "За все время:"
    Text(
        text = text,
        style = EdTheme.typography.labelSmall,
        color = EdTheme.colorScheme.secondary,
    )
    ShowPerformance(if (isCurrent) performance.lastSemester else performance.allSemesters)
}

@Composable
fun ShowPerformance(marks: Map<String, Int>) {
    var performanceString = ""
    marks.forEach { (mark, percent) ->
        performanceString += "$mark - $percent%, "
    }
    WithContentAlpha(alpha = ContentAlpha.medium) {
        Text(
            text = performanceString.replaceAfterLast('%', ""),
            style = EdTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
