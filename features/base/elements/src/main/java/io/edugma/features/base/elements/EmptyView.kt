package io.edugma.features.base.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.base.core.utils.MaterialTheme3
import io.edugma.features.elements.R

@Composable
@Preview
fun EmptyView(message: String = "К сожалению, ничего не найдено.") {
    val anim = remember { R.raw.emptylist }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(anim))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpacerHeight(height = 50.dp)
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .fillMaxWidth()
        )
        SpacerHeight(height = 30.dp)
        Text(
            text = message,
            style = MaterialTheme3.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}