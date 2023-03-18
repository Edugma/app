package io.edugma.core.designSystem.atoms.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons

@Composable
fun EdLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        textAlign = textAlign,
        onTextLayout = onTextLayout,
        style = style,
    )
}

@Composable
fun EdLabel(
    text: String,
    iconPainter: Painter,
    modifier: Modifier = Modifier,
    iconStart: Boolean = true,
    iconSize: Dp = Dp.Unspecified,
    spacing: Dp = 6.dp,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
) {
    val textResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Layout(
        modifier = modifier,
        content = {
            val iconSizeModifier = if (iconSize.isUnspecified) {
                Modifier
            } else {
                Modifier.size(iconSize)
            }

            if (iconStart) {
                Icon(
                    painter = iconPainter,
                    contentDescription = "",
                    tint = if (color.isUnspecified) LocalContentColor.current else color,
                    modifier = Modifier
                        .layoutId("icon")
                        .padding(end = spacing)
                        .then(iconSizeModifier),
                )
            }
            Text(
                modifier = Modifier.layoutId("text"),
                text = text,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                letterSpacing = letterSpacing,
                textDecoration = textDecoration,
                lineHeight = lineHeight,
                overflow = overflow,
                softWrap = softWrap,
                maxLines = maxLines,
                textAlign = textAlign,
                onTextLayout = {
                    textResult.value = it
                    onTextLayout(it)
                },
                style = style,
            )
            if (iconStart.not()) {
                Icon(
                    painter = iconPainter,
                    contentDescription = "",
                    tint = if (color.isUnspecified) LocalContentColor.current else color,
                    modifier = Modifier
                        .layoutId("icon")
                        .padding(start = spacing)
                        .then(iconSizeModifier),
                )
            }
        },
    ) { measurables, constraints ->

        val textMeasurable = measurables.first { it.layoutId == "text" }
        val iconMeasurable = measurables.first { it.layoutId == "icon" }

        val iconPlaceable = measureIcon(
            constraints = constraints,
            iconMeasurable = iconMeasurable,
        )
        val textPlaceable = measureText(
            constraints = constraints,
            iconPlaceable = iconPlaceable,
            textMeasurable = textMeasurable,
        )

        val labelMeasures = calculatePositions(
            iconPlaceable = iconPlaceable,
            textPlaceable = textPlaceable,
            textResult = textResult,
            iconStart = iconStart,
        )

        val layoutWidth = maxOf(
            labelMeasures.textX + textPlaceable.width,
            labelMeasures.iconX + iconPlaceable.width,
            constraints.minWidth,
        )
        val layoutHeight = maxOf(
            iconPlaceable.height + labelMeasures.iconY,
            textPlaceable.height + labelMeasures.textY,
        )

        layout(layoutWidth, layoutHeight) {
            iconPlaceable.placeRelative(labelMeasures.iconX, labelMeasures.iconY)
            textPlaceable.placeRelative(labelMeasures.textX, labelMeasures.textY)
        }
    }
}

private fun calculatePositions(
    iconPlaceable: Placeable,
    textPlaceable: Placeable,
    textResult: MutableState<TextLayoutResult?>,
    iconStart: Boolean,
): EdLabelMeasures {
    val iconCenter = iconPlaceable.height / 2

    val iconY: Int
    val textY: Int

    // Center percent = 0.5. 0.56 is magic number
    // More percent - lower icon
    val almostCenterPercent = 0.565

    if (textPlaceable.height > iconPlaceable.height) {
        val firstLineHeight = requireNotNull(textResult.value).getLineBottom(0)
        val firstLineCenter = (firstLineHeight * almostCenterPercent).toInt()

        iconY = (firstLineCenter - iconCenter).coerceAtLeast(0)
        textY = (iconCenter - firstLineCenter).coerceAtLeast(0)
    } else {
        val textCenter = (textPlaceable.height * almostCenterPercent).toInt()

        textY = (iconCenter - textCenter).coerceAtLeast(0)
        iconY = (textCenter - iconCenter).coerceAtLeast(0)
    }

    val iconX: Int
    val textX: Int

    if (iconStart) {
        iconX = 0
        textX = iconPlaceable.width
    } else {
        iconX = textPlaceable.width
        textX = 0
    }

    return EdLabelMeasures(
        iconX = iconX,
        iconY = iconY,
        textX = textX,
        textY = textY,
    )
}

private class EdLabelMeasures(
    val iconX: Int,
    val iconY: Int,
    val textX: Int,
    val textY: Int,
)

private fun measureText(
    constraints: Constraints,
    iconPlaceable: Placeable,
    textMeasurable: Measurable,
): Placeable {
    val textConstraints = if (constraints.hasBoundedWidth) {
        constraints.copy(
            maxWidth = (constraints.maxWidth - iconPlaceable.width).coerceAtLeast(0),
            minWidth = (constraints.minWidth - iconPlaceable.width).coerceAtLeast(0),
            minHeight = 0,
        )
    } else {
        constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
    }
    return textMeasurable.measure(textConstraints)
}

private fun measureIcon(
    constraints: Constraints,
    iconMeasurable: Measurable,
) = iconMeasurable.measure(
    constraints.copy(
        minWidth = 0,
        minHeight = 0,
    ),
)

@Preview
@Composable
internal fun EdLabelPreview() {
    EdTheme {
        Column {
            EdLabel(
                text = "Sample my text",
                modifier = Modifier.size(50.dp),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "15 Sample",
                style = EdTheme.typography.bodySmall,
                iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_16_regular),
                spacing = 3.dp,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = true,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                spacing = 0.dp,
                fontSize = 8.sp,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = true,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                fontSize = 30.sp,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = false,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample",
                iconStart = false,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.width(100.dp),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample",
                iconStart = true,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample",
                iconStart = true,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text ".repeat(10),
                maxLines = 10,
                iconStart = true,
                iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
            )
            SpacerHeight(height = 8.dp)
            LazyRow {
                item {
                    EdLabel(
                        text = "Sample my text",
                        iconStart = false,
                        iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                    )
                }
                item {
                    SpacerWidth(width = 6.dp)
                }
                item {
                    EdLabel(
                        text = "Sample my text",
                        iconStart = false,
                        iconPainter = painterResource(id = EdIcons.ic_fluent_person_16_filled),
                    )
                }
            }
            SpacerHeight(height = 8.dp)
            LazyRow {
                item {
                    EdLabel(
                        text = "Sample my text",
                    )
                }
                item {
                    SpacerWidth(width = 6.dp)
                }
                item {
                    EdLabel(
                        text = "Sample my text",
                    )
                }
            }
        }
    }
}
