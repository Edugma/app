package io.edugma.features.account.marks.item

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import io.edugma.core.designSystem.molecules.chip.EdChip
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.account.marks.Filter
import io.edugma.features.base.core.utils.Typed1Listener


@Composable
fun FiltersRow(
    filters: Set<Filter<*>>,
    filterClickListener: Typed1Listener<Filter<*>>,
) {
    LazyRow {
        val filtersList = filters.toList()
        items(
            count = filtersList.size,
            key = { filtersList[it].hashCode() },
        ) {
            EdChip(
                icon = Icons.Rounded.Close,
                onClick = { filterClickListener.invoke(filtersList[it]) },
            ) {
                Text(
                    text = filtersList[it].mappedValue,
                    style = EdTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
