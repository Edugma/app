package io.edugma.core.designSystem.molecules.avatar

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdAvatar(
    url: String?,
    modifier: Modifier = Modifier,
    initials: String? = null,
) {
    if (url.isNullOrEmpty()) {
        val fixedInitials = initials?.take(5).orEmpty()
        val textSize = when (fixedInitials.length) {
            5 -> 11.sp
            4 -> 12.sp
            3 -> 13.sp
            2 -> 15.sp
            1 -> 15.sp
            else -> 10.sp
        }

        Card(
            shape = CircleShape,
            modifier = modifier
                .size(48.dp),
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = modifier
                .size(48.dp)
                .clip(CircleShape),
        )
    }
}
