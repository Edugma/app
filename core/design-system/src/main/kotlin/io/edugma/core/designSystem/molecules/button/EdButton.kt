package io.edugma.core.designSystem.molecules.button

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.loader.EdLoader
import io.edugma.core.designSystem.atoms.loader.EdLoaderStyle
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons

@Composable
fun EdButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    iconStart: Boolean = true,
    style: EdButtonStyle = EdButtonStyle.primary,
    size: EdButtonSize = EdButtonSize.medium,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .sizeIn(
                minWidth = size.minSize.width,
                minHeight = size.minSize.height,
            ),
        enabled = enabled,
        interactionSource = interactionSource,
        shape = size.shape,
        contentPadding = size.contentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = style.containerColor,
            contentColor = style.contentColor,
        ),
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EdLabel(
                text = text,
                iconPainter = iconPainter,
                iconStart = iconStart,
                modifier = Modifier.padding(bottom = 3.dp),
                style = size.textStyle,
                overflow = TextOverflow.Ellipsis,
            )
            if (isLoading) {
                SpacerWidth(width = size.spacer)
                EdLoader(
                    size = size.loaderSize,
                    style = EdLoaderStyle.content,
                )
            }
        }
    }
}

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
