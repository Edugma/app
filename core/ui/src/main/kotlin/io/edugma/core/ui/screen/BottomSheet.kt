package io.edugma.core.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.dragger.Dragger
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.tokens.shapes.EdShapes
import io.edugma.core.designSystem.tokens.shapes.top

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    header: String? = null,
    headerStyle: TextStyle = EdTheme.typography.headlineMedium,
    showDragger: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (showDragger) {
            SpacerHeight(height = 12.dp)
            Dragger(modifier = Modifier.align(CenterHorizontally))
            SpacerHeight(height = 3.dp)
        } else {
            SpacerHeight(height = 15.dp)
        }
        header?.let {
            EdLabel(
                text = header,
                style = headerStyle,
                modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 5.dp),
            )
        }
        content()
    }
}

@Composable
@Preview
internal fun BottomSheetPreview() {
    BottomSheet(
        modifier = Modifier.clip(EdShapes.extraLarge.top()).background(EdTheme.colorScheme.background),
        header = "header",
        showDragger = true,
    ) {
        EdLabel(text = "content")
    }
}
