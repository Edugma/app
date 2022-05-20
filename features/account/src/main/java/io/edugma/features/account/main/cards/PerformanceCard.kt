package io.edugma.features.account.main.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.features.account.main.CurrentPerformance
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.ContentAlpha
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.WithContentAlpha
import io.edugma.features.base.elements.TonalCard

@Composable
fun StudentsCard(
    performance: CurrentPerformance?,
    showCurrentPerformance: Boolean,
    onClick: ClickListener
) {
    TonalCard(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(85.dp)
            .fillMaxWidth(0.6f),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
            Column {
                Text(text = "Успеваемость")
                Spacer(Modifier.height(4.dp))
                performance?.apply {
                    if (showCurrentPerformance) {
                        Text(
                            text = "За ${performance.lastSemesterNumber} семестр:",
                            style = MaterialTheme3.typography.labelSmall,
                            color = MaterialTheme3.colorScheme.secondary
                        )
                        ShowPerformance(performance.lastSemester)
                    } else {
                        Text(
                            text = "За все время:",
                            style = MaterialTheme3.typography.labelSmall,
                            color = MaterialTheme3.colorScheme.secondary
                        )
                        ShowPerformance(performance.allSemesters)
                    }
                }
            }
        }
    }
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
            style = MaterialTheme3.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}