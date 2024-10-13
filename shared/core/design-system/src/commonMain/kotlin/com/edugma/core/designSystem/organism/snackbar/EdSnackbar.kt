package com.edugma.core.designSystem.organism.snackbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.card.EdCard
import com.edugma.core.designSystem.atoms.card.EdCardDefaults
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.molecules.textButton.EdTextButton
import com.edugma.core.designSystem.molecules.textButton.EdTextButtonSize
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation
import com.edugma.core.designSystem.utils.SecondaryContent
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration

@Composable
fun EdSnackbar(
    title: String,
    timeToDismiss: Duration,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String = "",
    action: String? = null,
    onActionClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    style: EdSnackbarStyle = EdSnackbarStyle.default,
) {
    // TODO Sometimes it doesn't dismissed after swipe
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(Unit) {
        snapshotFlow { dismissState.currentValue }.collect {
            if (it != SwipeToDismissBoxValue.Settled) {
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

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        content = {
            SnackbarContent(
                title = title,
                subtitle = subtitle,
                action = action,
                onActionClick = onActionClick,
                style = style,
                modifier = Modifier.padding(contentPadding),
            )
        },
        backgroundContent = {
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
            horizontal = 16.dp,
            vertical = 10.dp,
        ),
        elevation = EdElevation.Level3,
        colors = EdCardDefaults.cardColors(
            containerColor = style.backgroundColor,
            contentColor = style.contentColor.takeOrElse { LocalContentColor.current },
        ),
    ) {
        EdLabel(
            text = title,
            style = EdTheme.typography.bodyLarge,
            iconPainter = painterResource(style.iconRes),
        )
        SpacerHeight(4.dp)
        SecondaryContent {
            EdLabel(
                text = subtitle,
                style = EdTheme.typography.bodyMedium,
            )
        }
        if (action != null && onActionClick != null) {
            SpacerHeight(2.dp)
            EdTextButton(
                text = action,
                onClick = onActionClick,
                modifier = Modifier.align(Alignment.End),
                size = EdTextButtonSize.medium,
            )
        }
    }
}
