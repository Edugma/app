package io.edugma.core.designSystem.atoms.loader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdLoader(
    modifier: Modifier = Modifier,
    size: EdLoaderSize = EdLoaderSize.small,
    style: EdLoaderStyle = EdLoaderStyle.primary,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size.size),
        color = style.color,
        strokeWidth = size.strokeWidth,
    )
}

@Preview
@Composable
internal fun EdLoader() {
    EdTheme {
        Column(Modifier.padding(10.dp)) {
            val sizes = listOf(
                EdLoaderSize.extraSmall to "extraSmall",
                EdLoaderSize.small to "small",
                EdLoaderSize.medium to "medium",
                EdLoaderSize.large to "large",
            )

            sizes.forEach { (size, sizeLabel) ->
                Text(text = sizeLabel, style = EdTheme.typography.headlineSmall)
                Row {
                    Column {
                        Text(text = "primary")
                        EdLoader(
                            size = size,
                            style = EdLoaderStyle.primary,
                        )
                    }
                    Column {
                        Text(text = "secondary")
                        EdLoader(
                            size = size,
                            style = EdLoaderStyle.secondary,
                        )
                    }
                    Column {
                        Text(text = "content")
                        EdLoader(
                            size = size,
                            style = EdLoaderStyle.content,
                        )
                    }
                }
                SpacerHeight(height = 4.dp)
            }
        }
    }
}
