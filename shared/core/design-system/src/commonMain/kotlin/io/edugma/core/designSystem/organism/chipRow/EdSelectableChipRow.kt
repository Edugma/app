package io.edugma.core.designSystem.organism.chipRow

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.molecules.chip.EdChipForm
import io.edugma.core.designSystem.molecules.chip.EdSelectableChip
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder

@Composable
fun <T : Any> EdSelectableChipRow(
    types: List<T>,
    selectedType: T?,
    nameMapper: (T) -> String,
    modifier: Modifier = Modifier,
    chipForm: EdChipForm = EdChipForm.roundedSquare,
    defaultColor: Color = EdTheme.colorScheme.primary,
    selectedColor: Color = EdTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (T) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(
            count = types.size,
            // key = { types[it] },
        ) {
            EdSelectableChip(
                selectedState = types[it] == selectedType,
                chipForm = chipForm,
                defaultColor = defaultColor,
                selectedColor = selectedColor,
                onClick = { onClick.invoke(types[it]) },
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
    chipForm: EdChipForm = EdChipForm.roundedSquare,
) {
    LazyRow() {
        items(
            count = count,
        ) {
            EdSelectableChip(
                modifier = Modifier
                    .edPlaceholder()
                    .widthIn(80.dp),
                chipForm = chipForm,
            ) {}
        }
    }
}
