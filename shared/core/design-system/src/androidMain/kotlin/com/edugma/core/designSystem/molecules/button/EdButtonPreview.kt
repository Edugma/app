package com.edugma.core.designSystem.molecules.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource

@Preview
@Composable
internal fun EdButtonPrimaryPreview() {
    EdTheme {
        Column(Modifier.padding(10.dp)) {
            val sizes = listOf(
                EdButtonSize.small to "small",
                EdButtonSize.medium to "medium",
                EdButtonSize.large to "large",
            )

            sizes.forEach { (size, sizeLabel) ->
                Text(text = sizeLabel, style = EdTheme.typography.headlineSmall)
                Text(text = "Button with loader")
                EdButton(
                    text = "Press to win",
                    iconPainter = painterResource(EdIcons.ic_fluent_arrow_clockwise_12_regular),
                    onClick = { },
                    isLoading = true,
                    style = EdButtonStyle.primary,
                    size = size,
                )
                Text(text = "Button without loader")
                EdButton(
                    text = "Press to win",
                    iconPainter = painterResource(EdIcons.ic_fluent_arrow_clockwise_12_regular),
                    onClick = { },
                    isLoading = false,
                    style = EdButtonStyle.primary,
                    size = size,
                )
                SpacerHeight(height = 8.dp)
            }
        }
    }
}

@Preview
@Composable
internal fun EdButtonSecondaryPreview() {
    EdTheme {
        Column(Modifier.padding(10.dp)) {
            val sizes = listOf(
                EdButtonSize.small to "small",
                EdButtonSize.medium to "medium",
                EdButtonSize.large to "large",
            )

            sizes.forEach { (size, sizeLabel) ->
                Text(text = sizeLabel, style = EdTheme.typography.headlineSmall)
                Text(text = "Button with loader")
                EdButton(
                    text = "Press to win",
                    onClick = { },
                    isLoading = true,
                    style = EdButtonStyle.secondary,
                    size = size,
                )
                Text(text = "Button without loader")
                EdButton(
                    text = "Press to win",
                    onClick = { },
                    isLoading = false,
                    style = EdButtonStyle.secondary,
                    size = size,
                )
                SpacerHeight(height = 8.dp)
            }
        }
    }
}

@Preview
@Composable
internal fun EdButtonDisabledPreview() {
    EdTheme {
        Column(Modifier.padding(10.dp)) {
            val sizes = listOf(
                EdButtonSize.small to "small",
                EdButtonSize.medium to "medium",
                EdButtonSize.large to "large",
            )

            sizes.forEach { (size, sizeLabel) ->
                Text(text = sizeLabel, style = EdTheme.typography.headlineSmall)
                Text(text = "Button with loader")
                EdButton(
                    text = "Press to win",
                    onClick = { },
                    isLoading = true,
                    style = EdButtonStyle.primary,
                    size = size,
                    enabled = false,
                )
                Text(text = "Button without loader")
                EdButton(
                    text = "Press to win",
                    onClick = { },
                    isLoading = false,
                    style = EdButtonStyle.primary,
                    size = size,
                    enabled = false,
                )
                SpacerHeight(height = 8.dp)
            }
        }
    }
}
