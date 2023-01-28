package io.edugma.core.designSystem.atoms.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons

@Composable
fun EdLabel(
    text: String,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    iconStart: Boolean = true,
    spacing: Dp = 6.dp,
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
    maxLines: Int = 1,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (iconPainter != null && iconStart) {
            Icon(
                painter = iconPainter,
                contentDescription = "",
                tint = if (color.isUnspecified) LocalContentColor.current else color,
                modifier = Modifier
                    .padding(end = spacing),
            )
        }
        Text(
            text = text,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = onTextLayout,
            style = style,
        )
        if (iconPainter != null && iconStart.not()) {
            Icon(
                painter = iconPainter,
                contentDescription = "",
                tint = if (color.isUnspecified) LocalContentColor.current else color,
                modifier = Modifier
                    .padding(start = spacing),
            )
        }
    }
}

@Preview
@Composable
internal fun EdLabelPreview() {
    EdTheme {
        Column {
            EdLabel(
                text = "Sample my text",
                iconStart = true,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = false,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
            )
        }
    }
}
