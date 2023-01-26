package io.edugma.core.designSystem.organism.topAppBar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
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
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    onNavigationClick?.let { onNavigationClick() }
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
                    EdLabel(
                        text = subtitle,
                        iconPainter = subtitleIcon,
                    )
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
                        .padding(10.dp)
                        .ifNotNull(onNavigationClick) { onNavigationClick ->
                            clickable { onNavigationClick() }
                        },
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
