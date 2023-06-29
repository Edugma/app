package io.edugma.core.designSystem.organism.refresher

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun Refresher(text: String = "Произошла ошибка", onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EdLabel(
                text = text,
                iconPainter = painterResource(EdIcons.ic_fluent_arrow_clockwise_20_filled),
                spacing = 8.dp,
                iconStart = false,
                style = EdTheme.typography.bodyLarge,
            )
        }
    }
}
