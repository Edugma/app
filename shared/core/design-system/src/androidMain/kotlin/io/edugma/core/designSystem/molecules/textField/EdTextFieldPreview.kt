package io.edugma.core.designSystem.molecules.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.theme.EdTheme

@Preview
@Composable
fun EdTextFieldPreview() {
    EdTheme {
        Column {
            EdTextField(
                value = "My search text",
                onValueChange = { },
            )
            SpacerHeight(height = 10.dp)
            EdTextField(
                value = "",
                placeholder = "Enter search text",
                onValueChange = { },
            )
            SpacerHeight(height = 10.dp)
            EdTextField(
                value = "error text",
                isError = true,
                onValueChange = { },
            )
        }
    }
}
