package io.edugma.features.base.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.edugma.features.base.core.utils.*

@Composable
fun PrimaryTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBackClick: ClickListener,
    showLoading: Boolean = false,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {

    SmallTopAppBar(
        title = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.weight(1f, fill = true)
                ) {
                    Column(
                        Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme3.typography.titleMedium
                        )
                        if (subtitle != null) {
                            WithContentAlpha(alpha = ContentAlpha.medium) {
                                Text(
                                    text = subtitle,
                                    style = MaterialTheme3.typography.labelSmall
                                )
                            }
                        }
                    }
                }
                Box(Modifier.width(50.dp)) {
                    if (showLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(26.dp)
                                .align(Alignment.CenterEnd),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        },
        modifier.height(50.dp),
        navigationIcon = {
            BackIconButton(onBackClick)
        },
        actions,
        colors,
        scrollBehavior
    )
}

@Composable
fun BackIconButton(onBackClick: ClickListener) {
    IconButton(onClick = onBackClick) {
        Icon(painter = painterResource(FluentIcons.ic_fluent_arrow_left_20_filled), contentDescription = null)
    }
}