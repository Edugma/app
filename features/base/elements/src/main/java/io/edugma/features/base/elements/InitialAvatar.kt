package io.edugma.features.base.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import io.edugma.features.base.core.utils.MaterialTheme3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialAvatar(url: String, initials: String = "") {
    if (url.isEmpty()) {
        val fixedInitials = initials.take(5)
        val textSize = when (fixedInitials.length) {
            5 -> 11.sp
            4 -> 12.sp
            3 -> 13.sp
            2 -> 14.sp
            1 -> 14.sp
            else -> 10.sp
        }

        Card(
            shape = CircleShape,
            modifier = Modifier
                .height(48.dp)
                .width(48.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme3.colorScheme.surfaceVariant
            )
        ) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fixedInitials,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    fontSize = textSize
                )
            }
        }
    } else {
        Image(
            painter = rememberImagePainter(
                data = url,
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
    }
}