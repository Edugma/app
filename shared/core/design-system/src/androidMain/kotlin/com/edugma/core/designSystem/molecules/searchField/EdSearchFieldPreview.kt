package com.edugma.core.designSystem.molecules.searchField

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.edugma.core.designSystem.theme.EdTheme

@Preview
@Composable
fun EdSearchFieldPreview() {
    EdTheme {
        EdSearchField(
            value = "My search text",
            onValueChange = { },
        )
    }
}
