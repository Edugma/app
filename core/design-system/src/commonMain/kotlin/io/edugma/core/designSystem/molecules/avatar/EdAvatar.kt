package io.edugma.core.designSystem.molecules.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.rememberAsyncImagePainter

@Composable
fun EdAvatar(
    url: String?,
    modifier: Modifier = Modifier,
    size: EdAvatarSize = EdAvatarSize.medium,
    initials: String? = null,
) {
    if (url.isNullOrEmpty()) {
        val fixedInitials = initials?.take(5).orEmpty()
        val textSize = size.textSizes[fixedInitials.length]

        Card(
            shape = CircleShape,
            modifier = modifier
                .size(size.size),
            colors = CardDefaults.cardColors(
                containerColor = EdTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = fixedInitials,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontSize = textSize,
                )
            }
        }
    } else {
        val painter = rememberAsyncImagePainter(url)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .size(size.size)
                .clip(CircleShape),
        )
    }
}

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
