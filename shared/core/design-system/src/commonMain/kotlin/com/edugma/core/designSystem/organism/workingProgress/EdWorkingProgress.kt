package com.edugma.core.designSystem.organism.workingProgress

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
import com.edugma.core.designSystem.atoms.lottie.EdLottie
import com.edugma.core.designSystem.atoms.lottie.LottieSource
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme

@Composable
fun EdWorkingProgress(modifier: Modifier = Modifier, message: String = "В процессе разработки") {
    val anim = remember { "files/working.json" }
    Column(
        modifier = modifier.padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        EdLottie(
            lottieSource = LottieSource.FileRes(anim),
            backgroundColor = EdTheme.colorScheme.background,
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
