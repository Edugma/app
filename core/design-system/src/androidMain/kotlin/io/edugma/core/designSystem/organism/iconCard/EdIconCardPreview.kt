package io.edugma.core.designSystem.organism.iconCard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.icons.EdIcons
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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
