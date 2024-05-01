package io.edugma.core.designSystem.organism.iconCard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import io.edugma.core.designSystem.theme.EdTheme
import edugma.shared.core.icons.generated.resources.*
import io.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
private fun EdIconCardPreview() {
    EdTheme {
        EdIconCard(
            title = "История изменений",
            onClick = { },
            icon = painterResource(EdIcons.ic_fluent_person_24_filled),
        )
    }
}
