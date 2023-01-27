package io.edugma.features.base.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.icons.EdIcons
import io.edugma.features.base.core.utils.ClickListener
import io.edugma.features.elements.R

@Composable
@Preview
fun ErrorView(message: String = "Упс... Что-то пошло не так", retryAction: ClickListener = {}) {
    val anim = remember { R.raw.error }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(anim))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )
    Column(
        modifier = Modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SpacerHeight(height = 50.dp)
        Text(
            text = message,
            style = EdTheme.typography.headlineSmall,
        )
        SpacerHeight(height = 30.dp)
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier
                .fillMaxWidth(),
        )
        SpacerHeight(height = 30.dp)
        EdButton(
            onClick = retryAction,
            modifier = Modifier.fillMaxWidth(),
            text = "Повторить",
            iconPainter = painterResource(EdIcons.ic_fluent_arrow_clockwise_16_regular),
        )
    }
}
