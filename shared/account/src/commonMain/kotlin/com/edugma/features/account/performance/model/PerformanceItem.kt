package com.edugma.features.account.performance.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.chip.EdChip
import com.edugma.core.designSystem.molecules.chip.EdChipForm
import com.edugma.core.designSystem.molecules.chip.EdChipLabel
import com.edugma.core.designSystem.molecules.chip.EdChipSize
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.SecondaryContent
import com.edugma.core.designSystem.utils.edPlaceholder
import com.edugma.features.account.domain.model.performance.GradePosition
import com.edugma.features.account.domain.model.performance.GradeValue
import kotlin.math.roundToInt

@Composable
fun PerformanceItem(
    performance: GradePosition,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
            )
            .padding(vertical = 9.dp, horizontal = 12.dp),
    ) {
        Column(
            modifier = modifier
                .weight(1f),
        ) {
            EdChipLabel(
                text = performance.type,
                size = EdChipSize.small,
                modifier = Modifier.padding(bottom = 3.dp)
            )
            EdLabel(
                text = performance.title,
                style = EdTheme.typography.titleSmall,
                modifier = Modifier,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            SpacerHeight(2.dp)
            SecondaryContent {
                EdLabel(
                    text = performance.description,
                    style = EdTheme.typography.bodyMedium,
                    modifier = Modifier,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        val gradeColor = when (performance.grade?.normalizedValue) {
            GradeValue.VERY_GOOD -> EdTheme.customColorScheme.success
            GradeValue.GOOD -> EdTheme.customColorScheme.success
            GradeValue.NORMAL -> EdTheme.customColorScheme.warning
            GradeValue.BAD -> EdTheme.customColorScheme.warning
            GradeValue.VERY_BAD -> EdTheme.customColorScheme.warning
            null -> LocalContentColor.current
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
                .padding(start = 4.dp),
        ) {
            val progress = performance.grade?.let { grade ->
                val range = grade.maxValue - grade.minValue
                val normValue = grade.value - grade.minValue
                normValue / range
            } ?: .0
            CircularProgressIndicator(
                progress = progress.toFloat(),
                color = gradeColor,
                modifier = Modifier.size(50.dp),
                strokeCap = StrokeCap.Round,
            )

            if (performance.grade != null) {
                val gradeText = if (
                    performance.grade.maxValue == 1.0 &&
                    performance.grade.minValue == .0
                ) {
                    if (performance.grade.value == 1.0) {
                        "✔"
                    } else {
                        "✘"
                    }
                } else {
                    performance.grade.value.roundToInt().toString()
                }
                Text(
                    text = gradeText,
                    style = EdTheme.typography.titleLarge,
                    color = gradeColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .width(50.dp)
                        .padding(vertical = 5.dp),
                )
            }
        }
    }
}

@Composable
fun PerformancePlaceholder() {
    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 120.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        EdLabel(
            text = "performancermance",
            style = EdTheme.typography.titleMedium.copy(fontSize = 19.sp),
            modifier = Modifier
                .heightIn(30.dp)
                .edPlaceholder(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = "performance",
            style = EdTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = 5.dp)
                .edPlaceholder(),
        )
        Row {
            EdChip(
                chipForm = EdChipForm.roundedSquare,
                modifier = Modifier.edPlaceholder(),
            )
            EdChip(
                chipForm = EdChipForm.roundedSquare,
                modifier = Modifier.edPlaceholder(),
            )
        }
    }
}
