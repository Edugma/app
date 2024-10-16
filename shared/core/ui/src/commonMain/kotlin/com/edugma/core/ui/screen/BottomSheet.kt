package com.edugma.core.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.label.EdLabel
import com.edugma.core.designSystem.theme.EdTheme

@Composable
fun ColumnScope.BottomSheet(
    modifier: Modifier = Modifier,
    title: String? = null,
    headerStyle: TextStyle = EdTheme.typography.headlineMedium,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 5.dp,
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    title?.let {
        EdLabel(
            text = title,
            style = headerStyle,
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, bottom = 5.dp),
        )
    }
    Column(
        modifier = modifier
            .padding(contentPadding),
    ) {
        content()
    }
}
