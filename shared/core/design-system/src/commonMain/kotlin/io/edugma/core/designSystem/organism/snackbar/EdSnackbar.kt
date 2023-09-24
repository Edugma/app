package io.edugma.core.designSystem.organism.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.card.EdCardDefaults
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.atoms.spacer.SpacerWidth
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.molecules.button.EdButtonSize
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.utils.SecondaryContent
import kotlinx.coroutines.delay
import kotlin.time.Duration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdSnackbar(
    title: String,
    timeToDismiss: Duration,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    action: String? = null,
    onActionClick: (() -> Unit)? = null,
    style: EdSnackbarStyle = EdSnackbarStyle.default,
) {
    // TODO Sometimes it doesn't dismissed after swipe
    val dismissState = rememberDismissState()

    LaunchedEffect(Unit) {
        snapshotFlow { dismissState.currentValue }.collect {
            if (it != DismissValue.Default) {
                onDismissed()
            }
        }
    }

    if (timeToDismiss.isFinite()) {
        LaunchedEffect(title, subtitle) {
            delay(timeToDismiss)
            onDismissed()
        }
    }

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        dismissContent = {
            SnackbarContent(
                title = title,
                subtitle = subtitle,
                action = action,
                onActionClick = onActionClick,
                style = style,
            )
        },
        background = {
        },
    )
}

@Composable
private fun SnackbarContent(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    action: String? = null,
    onActionClick: (() -> Unit)? = null,
    style: EdSnackbarStyle = EdSnackbarStyle.default,
) {
    EdCard(
        modifier = modifier.alpha(0.98f).fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 8.dp,
            vertical = 8.dp,
        ),
        elevation = EdElevation.Level3,
        colors = EdCardDefaults.cardColors(
            containerColor = style.backgroundColor,
            contentColor = style.contentColor.takeOrElse { LocalContentColor.current },
        ),
    ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(style.iconRes),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                colorFilter = ColorFilter.tint(LocalContentColor.current),
            )
            SpacerWidth(8.dp)
            Column(Modifier.fillMaxWidth()) {
                EdLabel(
                    text = title,
                    style = EdTheme.typography.bodyLarge,
                )
                SpacerHeight(6.dp)
                SecondaryContent {
                    EdLabel(
                        text = subtitle,
                        style = EdTheme.typography.bodySmall,
                    )
                }
            }
        }
        if (action != null && onActionClick != null) {
            SpacerHeight(6.dp)
            EdButton(
                text = action,
                onClick = onActionClick,
                size = EdButtonSize.small,
                modifier = Modifier.align(Alignment.End),
            )
        }
    }
}
