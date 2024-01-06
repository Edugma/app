package io.edugma.core.designSystem.organism.errorWithRetry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.atoms.lottie.rememberLottiePainter
import io.edugma.core.designSystem.organism.fullScreenMessage.EdFullScreenMessage
import io.edugma.core.resources.MR
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * @param title Reason for error.
 * @param subtitle What the user to do.
 * @param onRetry Action on retry click.
 */
@OptIn(ExperimentalResourceApi::class)
@Composable
fun EdErrorRetry(
    modifier: Modifier = Modifier,
    title: String = "Что-то пошло не так",
    subtitle: String = "Попробуйте обновить страницу",
    onRetry: () -> Unit = {},
) {
    val painter = rememberLottiePainter(
        source = LottieSource.FileRes(MR.files.error),
        alternativeUrl = "https://raw.githubusercontent.com/Edugma/resources/main/42410-sleeping-polar-bear.gif",
    )

    EdFullScreenMessage(
        title = title,
        subtitle = subtitle,
        painter = painter,
        modifier = modifier,
        onAction = onRetry,
        actionTitle = "Обновить",
    )
}
