package io.edugma.features.base.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.isNotNull
import io.edugma.features.base.core.utils.isNull

@Composable
fun Chip(modifier: Modifier = Modifier, icon: ImageVector? = null, onClick: (() -> Unit)? = null, body: @Composable () -> Unit = {}) {
    TonalCard(modifier = Modifier
        .padding(4.dp)
        .height(32.dp),
        shape = MaterialTheme3.shapes.extraLarge,
    ) { Row(modifier = modifier
        .padding(end = 15.dp, start = 5.dp)
        .widthIn(min = 50.dp, max = 100.dp)
        .let { onClick?.let { listener -> if (icon.isNull()) it.clickable(onClick = listener) else it } ?: it},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = {
            if (icon.isNotNull()) {
                Icon(imageVector = icon!!, contentDescription = null,
                    modifier = Modifier
                        .let { onClick?.let { listener -> it.clickable(onClick = listener) } ?: it }
                        .size(20.dp)
                )
            } else SpacerWidth(width = 10.dp)
            body.invoke()
        }) }
}

@Composable
fun SelectableChip(modifier: Modifier = Modifier, selectedState: Boolean = false, onClick: (() -> Unit)? = null, body: @Composable () -> Unit = {}) {
    TonalCard(modifier = Modifier
        .padding(4.dp)
        .height(32.dp),
        color = if (selectedState) MaterialTheme3.colorScheme.primary else MaterialTheme3.colorScheme.surface,
        shape = MaterialTheme3.shapes.extraLarge,
    ) { Row(modifier = modifier
        .widthIn(min = 100.dp)
        .let { onClick?.let { listener -> it.clickable(onClick = listener) } ?: it},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = {
            SpacerWidth(width = 20.dp)
            body.invoke()
            SpacerWidth(width = 20.dp)
        }) }
}