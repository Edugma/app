package com.edugma.core.designSystem.organism.topAppBar

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme

@Preview
@Composable
internal fun EdTopAppBarPreview() {
    EdTheme {
        Column {
            EdTopAppBar(
                title = "Sample text",
            )
            SpacerHeight(height = 16.dp)
            EdTopAppBar(
                title = "Sample text",
                subtitle = "Sample text2",
            )
        }
    }
}
