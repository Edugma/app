package io.edugma.features.base.elements

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.ClickListener

@Composable
fun ButtonView(modifier: Modifier = Modifier, text: String, onClick: ClickListener) {
    Button(shape = RoundedCornerShape(4.dp), onClick = onClick, modifier = modifier.defaultMinSize(minWidth = 200.dp)) {
        Text(text = text)
    }
}