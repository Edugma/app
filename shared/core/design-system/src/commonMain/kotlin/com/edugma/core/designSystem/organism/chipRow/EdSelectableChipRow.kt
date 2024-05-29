package com.edugma.core.designSystem.organism.chipRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.api.model.ListItemUiModel
import com.edugma.core.api.model.contentType
import com.edugma.core.api.model.key
import com.edugma.core.designSystem.molecules.chip.EdChipLabel
import com.edugma.core.designSystem.molecules.chip.EdChipLabelPlaceholder
import com.edugma.core.designSystem.molecules.chip.EdChipSize

@Composable
fun <T : ListItemUiModel> EdChipLabelLazyRow(
    items: List<T>,
    selectedItem: T?,
    title: (T) -> String,
    modifier: Modifier = Modifier,
    chipSize: EdChipSize = EdChipSize.medium,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (T) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(
            items = items,
            key = items.key(),
            contentType = items.contentType(),
        ) {
            EdChipLabel(
                selected = it == selectedItem,
                text = title.invoke(it),
                onClick = { onClick.invoke(it) },
                size = chipSize,
            )
        }
    }
}

@Composable
fun EdChipLabelLazyRow(
    items: List<String>,
    selectedItem: String?,
    modifier: Modifier = Modifier,
    chipSize: EdChipSize = EdChipSize.medium,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onClick: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(
            count = items.size,
            key = { items[it] },
            contentType = { 0 },
        ) {
            EdChipLabel(
                selected = items[it] == selectedItem,
                text = items[it],
                onClick = { onClick.invoke(items[it]) },
                size = chipSize,
            )
        }
    }
}

@Composable
fun EdChipRowPlaceholders(
    count: Int = 2,
    chipSize: EdChipSize = EdChipSize.medium,
) {
    repeat(count) {
        EdChipLabelPlaceholder(
            size = chipSize,
            modifier = if (it == 0) {
                Modifier
            } else {
                Modifier.padding(end = 10.dp)
            },
        )
    }
}
