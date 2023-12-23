package io.edugma.core.designSystem.organism.cell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.molecules.avatar.EdAvatarSize
import io.edugma.core.designSystem.theme.EdTheme

@Immutable
data class EdCellSize(
    val avatarSize: EdAvatarSize,
    val titleTextStyle: TextStyle,
    val subtitleTextStyle: TextStyle,
    val titleSubtitleSpacing: Dp,
) {
    companion object {
        val medium: EdCellSize
            @Composable
            @ReadOnlyComposable
            get() = EdCellSize(
                avatarSize = EdAvatarSize.large,
                titleTextStyle = EdTheme.typography.titleSmall,
                subtitleTextStyle = EdTheme.typography.bodySmall,
                titleSubtitleSpacing = 1.dp,
            )

        val large: EdCellSize
            @Composable
            @ReadOnlyComposable
            get() = EdCellSize(
                avatarSize = EdAvatarSize.extraLarge,
                titleTextStyle = EdTheme.typography.titleMedium,
                subtitleTextStyle = EdTheme.typography.bodySmall,
                titleSubtitleSpacing = 2.dp,
            )
    }
}
