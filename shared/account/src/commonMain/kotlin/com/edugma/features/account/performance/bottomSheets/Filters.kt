package com.edugma.features.account.performance.bottomSheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.chip.EdChip
import com.edugma.core.designSystem.molecules.chip.EdChipLabel
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.edPlaceholder
import com.edugma.core.ui.screen.BottomSheet
import com.edugma.core.utils.ClickListener
import com.edugma.core.utils.Typed1Listener
import com.edugma.features.account.performance.Filter
import com.edugma.features.account.performance.PerformanceUiState

@Composable
fun ColumnScope.FiltersBottomSheetContent(
    state: PerformanceUiState,
    filterUpdateListener: Typed1Listener<Filter<*>>,
    resetFilterListener: ClickListener,
) {
    BottomSheet(
        title = "Фильтры",
        headerStyle = EdTheme.typography.headlineSmall,
    ) {
        if (state.periods != null) {
            SelectableChipsRow(
                name = "Периоды",
                items = state.periods,
                onClick = filterUpdateListener,
                placeholders = state.bottomSheetPlaceholders,
            )
        }
        SpacerHeight(height = 15.dp)
        SelectableChipsRow(
            name = "Тип",
            items = state.types,
            onClick = filterUpdateListener,
            placeholders = state.bottomSheetPlaceholders,
        )
        SpacerHeight(height = 15.dp)
        EdButton(
            onClick = resetFilterListener,
            modifier = Modifier.fillMaxWidth(),
            text = "Сбросить",
            enabled = state.currentFilters.isNotEmpty(),
        )
    }
}

@Composable
private fun <T> SelectableChipsRow(
    name: String,
    items: List<Filter<T>>,
    onClick: Typed1Listener<Filter<T>>,
    placeholders: Boolean,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = EdTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        SpacerWidth(width = 10.dp)
        LazyRow {
            if (placeholders) {
                items(4) {
                    EdChip(Modifier.edPlaceholder()) {}
                }
            } else {
                val listItems = items.toList()
                items(
                    count = listItems.size,
                    key = { listItems[it].hashCode() },
                ) {
                    EdChipLabel(
                        text = listItems[it].mappedValue,
                        onClick = { onClick.invoke(listItems[it]) },
                        selected = listItems[it].isChecked,
                    )
                }
            }
        }
    }
}
