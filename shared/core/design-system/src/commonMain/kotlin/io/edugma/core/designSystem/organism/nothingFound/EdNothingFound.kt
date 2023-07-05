package io.edugma.core.designSystem.organism.nothingFound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.resources.MR
import io.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdNothingFound(modifier: Modifier = Modifier, message: String = "К сожалению, ничего не найдено.") {
    val anim = remember { MR.assets.emptylist }
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val painter = rememberLottiePainter(
            source = LottieSource.Asset(anim),
            alternativeUrl = "https://raw.githubusercontent.com/Edugma/resources/main/42410-sleeping-polar-bear.gif",
        )
        EdLottie(
            lottiePainter = painter,
            modifier = Modifier
                .fillMaxWidth(),
        )
        SpacerHeight(height = 30.dp)
        Text(
            text = message,
            style = EdTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
    }
}
