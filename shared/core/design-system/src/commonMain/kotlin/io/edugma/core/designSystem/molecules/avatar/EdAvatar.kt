package io.edugma.core.designSystem.molecules.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.AsyncImage

@Composable
fun EdAvatar(
    url: String?,
    modifier: Modifier = Modifier,
    size: EdAvatarSize = EdAvatarSize.medium,
    placeholderColor: Color = Color.Unspecified,
    initials: String? = null,
) {
    AsyncImage(
        model = url?.takeIf { it.isNotEmpty() },
        image = { painter ->
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .size(size.size)
                    .clip(CircleShape),
            )
        },
        placeholder = {
            val fixedInitials = initials?.take(5).orEmpty()
            val textSize = size.textSizes[fixedInitials.length]

            Card(
                shape = CircleShape,
                modifier = modifier
                    .size(size.size),
                colors = CardDefaults.cardColors(
                    containerColor = placeholderColor.takeOrElse { EdTheme.colorScheme.surfaceVariant },
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
        },
    )
}
