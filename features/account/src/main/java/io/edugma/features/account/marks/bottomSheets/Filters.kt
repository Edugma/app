package io.edugma.features.account.marks.bottomSheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.chip.EdChip
import io.edugma.core.designSystem.molecules.chip.EdChipForm
import io.edugma.core.designSystem.molecules.chip.EdSelectableChip
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.edPlaceholder
import io.edugma.core.ui.screen.BottomSheet
import io.edugma.features.account.marks.Filter
import io.edugma.features.account.marks.MarksState
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.Typed1Listener


@Composable
fun FiltersBottomSheetContent(
    state: MarksState,
    filterUpdateListener: Typed1Listener<Filter<*>>,
    resetFilterListener: ClickListener,
) {
    BottomSheet(
        header = "Фильтры",
        headerStyle = EdTheme.typography.headlineSmall,
        modifier = Modifier.navigationBarsPadding()
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            SelectableChipsRow(
                "Курс",
                state.courses,
                filterUpdateListener,
                state.bottomSheetPlaceholders,
            )
            SpacerHeight(height = 15.dp)
            SelectableChipsRow(
                "Семестр",
                state.semesters,
                filterUpdateListener,
                state.bottomSheetPlaceholders,
            )
            SpacerHeight(height = 15.dp)
            SelectableChipsRow(
                "Тип",
                state.types,
                filterUpdateListener,
                state.bottomSheetPlaceholders,
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
}

@Composable
private fun<T> SelectableChipsRow(
    name: String,
    items: Set<Filter<T>>,
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
                    EdSelectableChip(
                        selectedState = listItems[it].isChecked,
                        chipForm = EdChipForm.roundedSquare,
                        onClick = { onClick.invoke(listItems[it]) },
                    ) {
                        Text(
                            text = listItems[it].mappedValue,
                            style = EdTheme.typography.labelMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
