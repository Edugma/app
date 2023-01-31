package io.edugma.features.base.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.utils.Typed1Listener
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull

@Composable
fun Chip(modifier: Modifier = Modifier, icon: ImageVector? = null, onClick: (() -> Unit)? = null, body: @Composable () -> Unit = {}) {
    EdCard(
        modifier = Modifier
            .padding(4.dp)
            .height(32.dp),
        shape = EdTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier
                .padding(end = 15.dp, start = 5.dp)
                .widthIn(min = 50.dp, max = 100.dp)
                .let { onClick?.let { listener -> if (icon.isNull()) it.clickable(onClick = listener) else it } ?: it },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = {
                if (icon.isNotNull()) {
                    Icon(
                        imageVector = icon!!,
                        contentDescription = null,
                        modifier = Modifier
                            .let {
                                onClick?.let { listener -> it.clickable(onClick = listener) } ?: it
                            }
                            .size(20.dp),
                    )
                } else {
                    SpacerWidth(width = 10.dp)
                }
                body.invoke()
            },
        )
    }
}

@Composable
fun SelectableChip(modifier: Modifier = Modifier, selectedState: Boolean = false, onClick: (() -> Unit)? = null, body: @Composable () -> Unit = {}) {
    EdCard(
        modifier = Modifier
            .padding(4.dp)
            .height(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selectedState) {
                EdTheme.colorScheme.primary
            } else {
                EdTheme.colorScheme.surface
            },
        ),
        shape = EdTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = modifier
                .widthIn(min = 50.dp)
                .let { onClick?.let { listener -> it.clickable(onClick = listener) } ?: it },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            content = {
                SpacerWidth(width = 15.dp)
                body.invoke()
                SpacerWidth(width = 15.dp)
            },
        )
    }
}

@Composable
fun<T : Any> SelectableOneTypesRow(
    types: List<T>,
    selectedType: T?,
    nameMapper: (T) -> String,
    clickListener: Typed1Listener<T>,
) {
    LazyRow() {
        items(
            count = types.size,
            key = { types[it] },
        ) {
            SelectableChip(
                selectedState = types[it] == selectedType,
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
fun SelectableTypesRowPlaceholders(count: Int = 2) {
    LazyRow() {
        items(
            count = count,
        ) {
            SelectableChip(
                modifier = Modifier
                    .placeholder(true)
                    .widthIn(80.dp),
            ) {}
        }
    }
}
