package io.edugma.features.base.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.base.core.utils.Typed1Listener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckBox(modifier: Modifier = Modifier, title: String, value: Boolean, onValueChange: Typed1Listener<Boolean>) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = value, onCheckedChange = onValueChange)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            style = MaterialTheme3.typography.titleMedium,
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null) { onValueChange.invoke(!value) },
        )
    }
}
