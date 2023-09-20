package io.edugma.core.designSystem.organism.snackbar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.card.EdCard
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.elevation.EdElevation
import io.edugma.core.designSystem.utils.SecondaryContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EdSnackbar(
    title: String,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String = "",
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

    LaunchedEffect(title, subtitle) {
        delay(6.seconds)
        onDismissed()
    }

    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        dismissContent = {
            SnackbarContent(
                title = title,
                subtitle = subtitle,
            )
        },
        background = {
        },
    )
}

@Composable
fun rememberSnackbarState(): SnackbarState {
    return remember {
        SnackbarState()
    }
}

class SnackbarState {
    suspend fun dismiss() {
    }
}

@Composable
private fun SnackbarContent(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String = "",
) {
    EdCard(
        modifier = modifier.alpha(0.98f).fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp,
        ),
        elevation = EdElevation.Level3,
    ) {
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
