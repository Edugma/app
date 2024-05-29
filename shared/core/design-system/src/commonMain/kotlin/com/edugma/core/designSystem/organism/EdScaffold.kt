package com.edugma.core.designSystem.organism

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.surface.EdSurface
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.shapes.bottom
import com.edugma.core.designSystem.tokens.shapes.top

// @Composable
// fun EdScaffold(
//    topBar: @Composable () -> Unit,
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit,
// ) {
//    val shape = EdTheme.shapes.large.bottom()
//    val shapeRadius = EdShapeTokens.CornerLargeRadius
//
//    Layout(
//        contents = listOf(
//            {
//                EdSurface(
//                    Modifier.fillMaxWidth(),
//                    shape = shape.bottom(),
//                ) {
//                    topBar()
//                }
//            },
//            {
//                content()
//            },
//        ),
//        modifier = modifier,
//    ) { (topBarMeasurable, contentMeasurable), constraints ->
//        val topBarPlaceable = topBarMeasurable.first().measure(constraints)
//
//        val contentTopY = (topBarPlaceable.height - shapeRadius.toPx())
//            .coerceAtLeast(0f).toInt()
//
//        val contentMaxHeight = if (constraints.hasBoundedHeight) {
//            constraints.maxHeight - contentTopY
//        } else {
//            constraints.maxHeight
//        }
//
//        val contentPlaceable = contentMeasurable.first().measure(
//            constraints.copy(
//                maxHeight = contentMaxHeight,
//                minHeight = constraints.minHeight.coerceAtMost(contentMaxHeight)
//            )
//        )
//
//        val scaffoldWidth = maxOf(topBarPlaceable.width, contentPlaceable.width)
//        val scaffoldHeight = contentTopY + contentPlaceable.height
//
//        layout(scaffoldWidth, scaffoldHeight) {
//            contentPlaceable.placeRelative(0, contentTopY)
//            topBarPlaceable.placeRelative(0, 0)
//        }
//    }
// }

@Composable
fun EdScaffold(
    topBar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = EdTheme.shapes.large

    Column(modifier) {
        EdSurface(
            Modifier.fillMaxWidth(),
            shape = shape.bottom(),
        ) {
            topBar()
        }
        Column(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .clip(shape.top()),
        ) {
            content()
        }
    }
}
