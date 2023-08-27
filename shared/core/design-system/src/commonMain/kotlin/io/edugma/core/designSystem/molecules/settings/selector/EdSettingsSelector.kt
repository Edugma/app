package io.edugma.core.designSystem.molecules.settings.selector

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.surface.EdSurface
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.SecondaryContent
import io.edugma.core.icons.EdIcons

@Composable
fun EdSettingsSelector(
    text: String,
    selectedText: String,
    icon: Painter?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EdSurface(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        onClick = onClick,
        shape = EdTheme.shapes.medium,
    ) {
        Row(
            Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon == null) {
                Box(
                    Modifier.padding(start = 16.dp)
                        .size(20.dp),
                )
            } else {
                Image(
                    painter = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp),
                )
            }
            EdLabel(
                text = text,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
            )
            SecondaryContent {
                EdLabel(
                    text = selectedText,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 12.dp),
                )
            }
            Image(
                painter = painterResource(EdIcons.ic_fluent_chevron_right_20_filled),
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(20.dp),
            )
        }
    }
}
