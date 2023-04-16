package io.edugma.core.designSystem.organism.chipRow

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.molecules.chip.EdChipSize
import io.edugma.core.designSystem.molecules.chip.EdSelectableChip
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder

@Composable
fun<T : Any> EdSelectableChipRow(
    types: List<T>,
    selectedType: T?,
    nameMapper: (T) -> String,
    chipSize: EdChipSize = EdChipSize.roundedSquare,
    defaultColor: Color = EdTheme.colorScheme.primary,
    selectedColor: Color = EdTheme.colorScheme.surface,
    clickListener: (T) -> Unit,
) {
    LazyRow() {
        items(
            count = types.size,
            key = { types[it] },
        ) {
            EdSelectableChip(
                selectedState = types[it] == selectedType,
                chipSize = chipSize,
                defaultColor = defaultColor,
                selectedColor = selectedColor,
                onClick = { clickListener.invoke(types[it]) },
            ) {
                Text(
                    text = nameMapper.invoke(types[it]),
                    style = EdTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun EdSelectableChipRowPlaceholders(
    count: Int = 2,
    chipSize: EdChipSize = EdChipSize.roundedSquare,
) {
    LazyRow() {
        items(
            count = count,
        ) {
            EdSelectableChip(
                modifier = Modifier
                    .edPlaceholder()
                    .widthIn(80.dp),
                chipSize = chipSize,
            ) {}
        }
    }
}
