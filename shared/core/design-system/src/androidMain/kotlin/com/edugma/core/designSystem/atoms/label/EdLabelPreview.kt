package com.edugma.core.designSystem.atoms.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import edugma.shared.core.icons.generated.resources.Res
import edugma.shared.core.icons.generated.resources.*
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.atoms.spacer.SpacerWidth
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
internal fun EdLabelPreview() {
    EdTheme {
        Column {
            EdLabel(
                text = "Sample my text",
                modifier = Modifier.size(50.dp),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "15 Sample",
                style = EdTheme.typography.bodySmall,
                iconPainter = painterResource(EdIcons.ic_fluent_calendar_ltr_16_regular),
                spacing = 3.dp,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = true,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                spacing = 0.dp,
                fontSize = 8.sp,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = true,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                fontSize = 30.sp,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text",
                iconStart = false,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text Sample my text Sample my text",
                iconStart = false,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.width(170.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample",
                iconStart = false,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.width(100.dp),
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample",
                iconStart = true,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample",
                iconStart = true,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            SpacerHeight(height = 8.dp)
            EdLabel(
                text = "Sample my text ".repeat(10),
                maxLines = 10,
                iconStart = true,
                iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
            )
            SpacerHeight(height = 8.dp)
            LazyRow {
                item {
                    EdLabel(
                        text = "Sample my text",
                        iconStart = false,
                        iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                    )
                }
                item {
                    SpacerWidth(width = 6.dp)
                }
                item {
                    EdLabel(
                        text = "Sample my text",
                        iconStart = false,
                        iconPainter = painterResource(EdIcons.ic_fluent_person_16_filled),
                    )
                }
            }
            SpacerHeight(height = 8.dp)
            LazyRow {
                item {
                    EdLabel(
                        text = "Sample my text",
                    )
                }
                item {
                    SpacerWidth(width = 6.dp)
                }
                item {
                    EdLabel(
                        text = "Sample my text",
                    )
                }
            }
        }
    }
}
