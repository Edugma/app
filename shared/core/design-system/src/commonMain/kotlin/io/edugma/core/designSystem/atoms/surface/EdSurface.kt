package io.edugma.core.designSystem.atoms.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.tokens.elevation.LocalEdElevation
import io.edugma.core.designSystem.utils.surfaceColorAtElevation

@Composable
@NonRestartableComposable
fun EdSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = EdTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    elevation: EdElevation = LocalEdElevation.current,
    elevatedAlpha: Float = 1f,
    border: BorderStroke? = null,
    content: @Composable () -> Unit,
) {
    val absoluteElevation = LocalAbsoluteTonalElevation.current + elevation.defaultElevation
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalAbsoluteTonalElevation provides absoluteElevation,
        LocalEdElevation provides elevation.getNextLevel(),
    ) {
        Box(
            modifier = modifier
                .surface(
                    shape = shape,
                    backgroundColor = surfaceColorAtElevation(
                        color = color,
                        elevation = absoluteElevation,
                        elevatedAlpha = elevatedAlpha,
                    ),
                    border = border,
                )
                .semantics(mergeDescendants = false) {}
                .pointerInput(Unit) {},
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}

@Composable
@NonRestartableComposable
fun EdSurface(
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = EdTheme.colorScheme.surface,
    selectedColor: Color = EdTheme.colorScheme.secondaryContainer,
    contentColor: Color = contentColorFor(color),
    selectedContentColor: Color = contentColorFor(selectedColor),
    elevation: EdElevation = LocalEdElevation.current,
    elevatedAlpha: Float = 1f,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val finalContentColor = if (selected) {
        selectedContentColor
    } else {
        contentColor
    }

    val absoluteElevation = LocalAbsoluteTonalElevation.current + elevation.defaultElevation
    CompositionLocalProvider(
        LocalContentColor provides finalContentColor,
        LocalAbsoluteTonalElevation provides absoluteElevation,
        LocalEdElevation provides elevation.getNextLevel(),
    ) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = if (selected) {
                        selectedColor
                    } else {
                        surfaceColorAtElevation(
                            color = color,
                            elevation = absoluteElevation,
                            elevatedAlpha = elevatedAlpha,
                        )
                    },
                    border = border,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                ),
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}

@Composable
@NonRestartableComposable
fun EdSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = EdTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    elevation: EdElevation = LocalEdElevation.current,
    elevatedAlpha: Float = 1f,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val absoluteElevation = LocalAbsoluteTonalElevation.current + elevation.defaultElevation
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
        LocalAbsoluteTonalElevation provides absoluteElevation,
        LocalEdElevation provides elevation.getNextLevel(),
    ) {
        Box(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .surface(
                    shape = shape,
                    backgroundColor = surfaceColorAtElevation(
                        color = color,
                        elevation = absoluteElevation,
                        elevatedAlpha = elevatedAlpha,
                    ),
                    border = border,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(),
                    enabled = enabled,
                    role = Role.Button,
                    onClick = onClick,
                ),
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}

// @OptIn(ExperimentalMaterial3Api::class)
// @Suppress("ModifierInspectorInfo")
// internal fun Modifier.minimumTouchTargetSize(): Modifier = composed(
//    inspectorInfo = debugInspectorInfo {
//        name = "minimumTouchTargetSize"
//        // TODO: b/214589635 - surface this information through the layout inspector in a better way
//        //  - for now just add some information to help developers debug what this size represents.
//        properties["README"] = "Adds outer padding to measure at least 48.dp (default) in " +
//            "size to disambiguate touch interactions if the element would measure smaller"
//    },
// ) {
//    if (LocalMinimumTouchTargetEnforcement.current) {
//        // TODO: consider using a hardcoded value of 48.dp instead to avoid inconsistent UI if the
//        // LocalViewConfiguration changes across devices / during runtime.
//        val size = LocalViewConfiguration.current.minimumTouchTargetSize
//        MinimumTouchTargetModifier(size)
//    } else {
//        Modifier
//    }
// }

// private class MinimumTouchTargetModifier(val size: DpSize) : LayoutModifier {
//    override fun MeasureScope.measure(
//        measurable: Measurable,
//        constraints: Constraints,
//    ): MeasureResult {
//
//        val placeable = measurable.measure(constraints)
//
//        // Be at least as big as the minimum dimension in both dimensions
//        val width = maxOf(placeable.width, size.width.roundToPx())
//        val height = maxOf(placeable.height, size.height.roundToPx())
//
//        return layout(width, height) {
//            val centerX = ((width - placeable.width) / 2f).roundToInt()
//            val centerY = ((height - placeable.height) / 2f).roundToInt()
//            placeable.place(centerX, centerY)
//        }
//    }
//
//    override fun equals(other: Any?): Boolean {
//        val otherModifier = other as? MinimumTouchTargetModifier
//            ?: return false
//        return size == otherModifier.size
//    }
//
//    override fun hashCode(): Int {
//        return size.hashCode()
//    }
// }

private fun Modifier.surface(
    shape: Shape,
    backgroundColor: Color,
    border: BorderStroke?,
) = this.then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)

@Composable
private fun surfaceColorAtElevation(
    color: Color,
    elevation: Dp,
    elevatedAlpha: Float,
): Color {
    return if (color == EdTheme.colorScheme.surface) {
        EdTheme.colorScheme.surfaceColorAtElevation(elevation).copy(alpha = elevatedAlpha)
    } else {
        color
    }
}
