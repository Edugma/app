package io.edugma.core.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.atoms.spacer.SpacerHeight
import io.edugma.core.designSystem.molecules.dragger.Dragger
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.ifThen

@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    header: String? = null,
    headerStyle: TextStyle = EdTheme.typography.headlineMedium,
    showDragger: Boolean = true,
    navigationBarPadding: Boolean = true,
    imePadding: Boolean = false,
    horizontalContentPadding: Dp = 16.dp,
    verticalContentPadding: Dp = 5.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .ifThen(navigationBarPadding) { navigationBarsPadding() }
            .ifThen(imePadding) { imePadding() },
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
        Column(modifier = modifier.padding(horizontal = horizontalContentPadding, vertical = verticalContentPadding)) {
            content()
        }
    }
}
