package io.edugma.core.designSystem.organism.nothingFound

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.organism.fullScreenMessage.EdFullScreenMessage

@Composable
fun EdNothingFound(
    modifier: Modifier = Modifier,
    title: String = "К сожалению, ничего не найдено",
    subtitle: String? = null,
) {
    EdFullScreenMessage(
        title = title,
        subtitle = subtitle,
        lottieSource = LottieSource.FileRes("files/emptylist.json"),
        modifier = modifier,
        onAction = null,
        actionTitle = null,
    )
}
