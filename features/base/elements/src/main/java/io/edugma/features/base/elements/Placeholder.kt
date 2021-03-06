package io.edugma.features.base.elements

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.placeholder


fun Modifier.placeholder(visible: Boolean,
                         color: Color = Color.Gray,
                         fadeColor: Color = Color.DarkGray,
                         shape: RoundedCornerShape = RoundedCornerShape(4.dp)) =
    placeholder(
        visible = visible,
        color = color,
        shape = shape,
        highlight = PlaceholderHighlight.fade(highlightColor = fadeColor)
    )