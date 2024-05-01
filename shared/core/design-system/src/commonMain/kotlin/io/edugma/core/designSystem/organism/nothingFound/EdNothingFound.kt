package io.edugma.core.designSystem.organism.nothingFound

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edugma.shared.core.resources.generated.resources.Res
import edugma.shared.core.resources.generated.resources.*
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.organism.fullScreenMessage.EdFullScreenMessage

@Composable
fun EdNothingFound(
    modifier: Modifier = Modifier,
    title: String = "К сожалению, ничего не найдено",
    subtitle: String? = null,
) {
    val painter = rememberLottiePainter(
        source = LottieSource.FileRes("files/emptylist.json"),
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
