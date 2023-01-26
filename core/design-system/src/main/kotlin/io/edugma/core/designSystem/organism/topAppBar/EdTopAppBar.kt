package io.edugma.core.designSystem.organism.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.core.designSystem.utils.ifNotNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    titleIcon: Painter? = null,
    subtitleIcon: Painter? = null,
    navigationIcon: Painter? = painterResource(EdIcons.ic_fluent_arrow_left_24_filled),
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = WindowInsets(0.dp), // TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            if (subtitle == null) {
                EdLabel(
                    text = title,
                    iconPainter = titleIcon,
                )
            } else {
                Column {
                    EdLabel(
                        text = title,
                        iconPainter = titleIcon,
                    )
                    CompositionLocalProvider(LocalContentColor provides EdTheme.colorScheme.onSurfaceVariant) {
                        EdLabel(
                            text = subtitle,
                            iconPainter = subtitleIcon,
                            style = EdTheme.typography.bodySmall,
                        )
                    }
                }
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (navigationIcon != null) {
                Icon(
                    painter = navigationIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .ifNotNull(onNavigationClick) { onNavigationClick ->
                            clickable(
                                onClick = { onNavigationClick() },
                                enabled = true,
                                role = Role.Button,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = false,
                                    radius = 24.dp,
                                ),
                            )
                        }.padding(12.dp),
                )
            }
        },
        actions = actions,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun EdTopAppBarPreview() {
    EdTheme {
        Column {
            EdTopAppBar(
                title = "Sample text",
            )
            SpacerHeight(height = 16.dp)
            EdTopAppBar(
                title = "Sample text",
                subtitle = "Sample text2",
            )
        }
    }
}
