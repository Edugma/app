package com.edugma.core.designSystem.organism.errorWithRetry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.edugma.core.designSystem.atoms.lottie.LottieSource
import com.edugma.core.designSystem.organism.fullScreenMessage.EdFullScreenMessage

/**
 * @param title Reason for error.
 * @param subtitle What the user to do.
 * @param onRetry Action on retry click.
 */
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
