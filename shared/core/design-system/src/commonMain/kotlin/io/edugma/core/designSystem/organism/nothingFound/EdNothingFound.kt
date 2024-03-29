package io.edugma.core.designSystem.organism.nothingFound

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.organism.fullScreenMessage.EdFullScreenMessage
import io.edugma.core.resources.MR

@Composable
fun EdNothingFound(
    modifier: Modifier = Modifier,
    title: String = "К сожалению, ничего не найдено",
    subtitle: String? = null,
) {
    val painter = rememberLottiePainter(
        source = LottieSource.FileRes(MR.files.emptylist),
        alternativeUrl = "https://raw.githubusercontent.com/Edugma/resources/main/42410-sleeping-polar-bear.gif",
    )

    EdFullScreenMessage(
        title = title,
        subtitle = subtitle,
        painter = painter,
        modifier = modifier,
        onAction = null,
        actionTitle = null,
    )
}
