package io.edugma.core.designSystem.organism.errorWithRetry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.edugma.core.designSystem.atoms.lottie.LottieSource
import io.edugma.core.designSystem.organism.fullScreenMessage.EdFullScreenMessage
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
    EdFullScreenMessage(
        title = title,
        subtitle = subtitle,
        lottieSource = LottieSource.FileRes("files/error.json"),
        modifier = modifier,
        onAction = onRetry,
        actionTitle = "Обновить",
    )
}
