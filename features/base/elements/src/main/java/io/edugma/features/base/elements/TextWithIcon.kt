package io.edugma.features.base.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
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

@Composable
fun TextIcon(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    iconTint: Color = LocalContentColor.current,
    space: Dp = 5.dp,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    Row(modifier) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        SpacerWidth(width = space)
        Text(
            text,
            modifier,
            color,
            fontSize,
            fontStyle,
            fontWeight,
            fontFamily,
            letterSpacing,
            textDecoration,
            textAlign,
            lineHeight,
            overflow,
            softWrap,
            maxLines,
            onTextLayout,
            style
        )
    }
}