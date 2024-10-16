package com.edugma.core.designSystem.atoms.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.spacer.SpacerHeight
import com.edugma.core.designSystem.theme.EdTheme
import com.edugma.core.designSystem.tokens.elevation.EdElevation

@Preview(
    showBackground = true,
    backgroundColor = 0xffffffff,
)
@Composable
fun EdCardPreview() {
    EdTheme {
        Column(Modifier.padding(4.dp)) {
            EdCard(
                elevation = EdElevation.Level1,
            ) {
                Box(modifier = Modifier.size(100.dp, 50.dp))
            }
            SpacerHeight(height = 10.dp)
            EdCard(
                elevation = EdElevation.Level2,
            ) {
                Box(modifier = Modifier.size(100.dp, 50.dp))
            }
            SpacerHeight(height = 10.dp)
            EdCard(
                elevation = EdElevation.Level3,
            ) {
                Box(modifier = Modifier.size(100.dp, 50.dp))
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xff000000,
)
@Composable
fun EdCardDarkPreview() {
    EdTheme(useDarkTheme = true) {
        Column(Modifier.padding(4.dp)) {
            EdCard(
                elevation = EdElevation.Level1,
            ) {
                Box(modifier = Modifier.size(100.dp, 50.dp))
            }
            SpacerHeight(height = 10.dp)
            EdCard(
                elevation = EdElevation.Level2,
            ) {
                Box(modifier = Modifier.size(100.dp, 50.dp))
            }
            SpacerHeight(height = 10.dp)
            EdCard(
                elevation = EdElevation.Level3,
            ) {
                Box(modifier = Modifier.size(100.dp, 50.dp))
            }
        }
    }
}
