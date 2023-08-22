package io.edugma.core.designSystem.organism.errorWithRetry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.lottie.EdLottie
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.button.EdButton
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons
import io.edugma.core.resources.MR
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ErrorWithRetry(modifier: Modifier = Modifier, message: String = "Упс... Что-то пошло не так", retryAction: () -> Unit = {}) {
    val anim = remember { MR.files.error }
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = EdTheme.typography.headlineSmall,
        )
        SpacerHeight(height = 30.dp)
        val painter = rememberLottiePainter(
            source = LottieSource.FileRes(anim),
            alternativeUrl = "https://raw.githubusercontent.com/Edugma/resources/main/42410-sleeping-polar-bear.gif",
        )
        EdLottie(
            lottiePainter = painter,
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
