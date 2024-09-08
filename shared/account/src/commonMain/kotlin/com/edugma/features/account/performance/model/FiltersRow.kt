package com.edugma.features.account.performance.model

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import com.edugma.core.designSystem.molecules.chip.EdChip
import com.edugma.core.designSystem.molecules.chip.EdChipForm
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import com.edugma.core.utils.Typed1Listener
import com.edugma.features.account.performance.Filter
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource

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
                iconPainter = painterResource(EdIcons.ic_fluent_dismiss_24_filled),
                chipForm = EdChipForm.roundedSquare,
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
