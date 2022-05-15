package io.edugma.features.base.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.MaterialTheme3

@Composable
fun Chip(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, body: @Composable () -> Unit) {
    TonalCard(modifier = modifier
            .padding(4.dp)
            .height(32.dp)
            .let { onClick?.let { listener -> it.clickable(onClick = listener) } ?: it},
        shape = MaterialTheme3.shapes.extraLarge,
    ) { Row(modifier = Modifier.padding(horizontal = 15.dp).widthIn(min = 50.dp),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
        content = { body.invoke() }) }
}