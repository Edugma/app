package com.edugma.core.designSystem.organism.iconCard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.icons.EdIcons
import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource

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
