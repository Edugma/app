package io.edugma.features.account.marks.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.edugma.core.api.utils.format
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.molecules.chip.EdChip
import io.edugma.core.designSystem.molecules.chip.EdChipForm
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.utils.ClickListener
import io.edugma.domain.account.model.Performance

@Composable
fun PerformanceItem(
    performance: Performance,
    onClick: ClickListener,
) {
    Column(
        modifier = Modifier
            .defaultMinSize(minHeight = 120.dp)
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        EdLabel(
            text = performance.name,
            style = EdTheme.typography.titleMedium.copy(fontSize = 19.sp),
            modifier = Modifier.heightIn(30.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = performance.grade,
            style = EdTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = 5.dp),
        )
        Row {
            performance.date?.let {
                EdChip(
                    chipForm = EdChipForm.roundedSquare,
                ) {
                    Text(
                        text = performance.date!!.format("dd.MM.yyyy"),
                        style = EdTheme.typography.labelLarge,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            EdChip(
                chipForm = EdChipForm.roundedSquare,
            ) {
                Text(
                    text = performance.examType,
                    style = EdTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
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
