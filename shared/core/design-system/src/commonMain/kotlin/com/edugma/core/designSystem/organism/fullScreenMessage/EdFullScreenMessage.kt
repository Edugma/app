package com.edugma.core.designSystem.organism.fullScreenMessage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.atoms.lottie.EdLottie
import com.edugma.core.designSystem.atoms.lottie.LottieSource
import com.edugma.core.designSystem.molecules.button.EdButton
import com.edugma.core.designSystem.molecules.button.EdButtonSize
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.utils.SecondaryContent

@Composable
fun EdFullScreenMessage(
    title: String,
    subtitle: String?,
    lottieSource: LottieSource,
    actionTitle: String?,
    modifier: Modifier = Modifier,
    onAction: (() -> Unit)? = null,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            EdLottie(
                lottieSource = lottieSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
            )
            EdLabel(
                text = title,
                style = EdTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp),
            )
            if (subtitle != null) {
                SecondaryContent {
                    EdLabel(
                        text = subtitle,
                        style = EdTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
        if (actionTitle != null && onAction != null) {
            EdButton(
                onClick = onAction,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 30.dp, bottom = 30.dp),
                text = actionTitle,
                size = EdButtonSize.large,
            )
        }
    }
}
