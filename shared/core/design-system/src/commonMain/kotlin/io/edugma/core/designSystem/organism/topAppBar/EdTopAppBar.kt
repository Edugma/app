package io.edugma.core.designSystem.organism.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.ifNotNull
import io.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun EdTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    titleIcon: Painter? = null,
    subtitleIcon: Painter? = null,
    navigationIcon: Painter? = painterResource(EdIcons.ic_fluent_chevron_left_20_filled),
    onNavigationClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = WindowInsets(0.dp), // TopAppBarDefaults.windowInsets,
    colors: EdTopAppBarColors = EdTopAppBarDefaults.colors(),
) {
    EdSingleRowTopAppBar(
        title = {
            if (subtitle == null) {
                if (titleIcon != null) {
                    EdLabel(
                        text = title,
                        iconPainter = titleIcon,
                        style = EdTheme.typography.titleMedium,
                    )
                } else {
                    EdLabel(
                        text = title,
                        style = EdTheme.typography.titleMedium,
                    )
                }
            } else {
                Column {
                    if (titleIcon != null) {
                        EdLabel(
                            text = title,
                            iconPainter = titleIcon,
                            style = EdTheme.typography.titleMedium,
                        )
                    } else {
                        EdLabel(
                            text = title,
                            style = EdTheme.typography.titleMedium,
                        )
                    }
                    CompositionLocalProvider(LocalContentColor provides EdTheme.colorScheme.onSurfaceVariant) {
                        if (subtitleIcon != null) {
                            EdLabel(
                                text = subtitle,
                                iconPainter = subtitleIcon,
                                style = EdTheme.typography.bodySmall,
                            )
                        } else {
                            EdLabel(
                                text = subtitle,
                                style = EdTheme.typography.bodySmall,
                            )
                        }
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
                        }
                        .padding(11.dp),
                )
            }
        },
        actions = actions,
        windowInsets = windowInsets,
        colors = colors,
        titleTextStyle = EdTheme.typography.titleLarge,
        centeredTitle = true,
    )
}
