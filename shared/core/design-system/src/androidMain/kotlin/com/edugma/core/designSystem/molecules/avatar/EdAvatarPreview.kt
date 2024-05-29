package com.edugma.core.designSystem.molecules.avatar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.edugma.core.designSystem.theme.EdTheme

@Preview
@Composable
fun EdAvatarPreview() {
    EdTheme {
        Column {
            EdAvatar(
                url = null,
                initials = "АБ",
                size = EdAvatarSize.small,
            )
            EdAvatar(
                url = null,
                initials = "АБ",
                size = EdAvatarSize.medium,
            )
            EdAvatar(
                url = null,
                initials = "АБ",
                size = EdAvatarSize.large,
            )
        }
    }
}
