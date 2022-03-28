package io.edugma.features.base.elements

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.MaterialTheme3

@Composable
fun TextWithIcon(modifier: Modifier = Modifier, text: String?, icon: ImageVector? = null) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
        icon?.let {
            Icon(icon, "", modifier = Modifier.padding(end = 8.dp))
        }
        Text(
            text = text.orEmpty(),
            style = MaterialTheme3.typography.bodyMedium,
            modifier = modifier
                .defaultMinSize(minWidth = 100.dp)
        )
    }
}

@Composable
fun TextWithIcon(modifier: Modifier = Modifier, text: String?, icon: Painter) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(5.dp)) {
        Icon(icon, "", modifier = Modifier.padding(end = 8.dp))
        Text(
            text = text.orEmpty(),
            style = MaterialTheme3.typography.bodyMedium,
            modifier = modifier
                .defaultMinSize(minWidth = 100.dp)
        )
    }
}