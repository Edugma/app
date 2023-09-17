package io.edugma.core.designSystem.molecules.dragger

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.edugma.core.designSystem.theme.EdTheme

@Preview
@Composable
fun DraggerPreview() {
    EdTheme {
        EdDragHandle()
    }
}
