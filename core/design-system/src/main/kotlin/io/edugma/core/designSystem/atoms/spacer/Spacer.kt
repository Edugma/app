package io.edugma.core.designSystem.atoms.spacer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun ColumnScope.SpacerHeight(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun RowScope.SpacerWidth(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun LazyItemScope.SpacerHeight(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun LazyItemScope.SpacerWidth(width: Dp) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun RowScope.SpacerFill(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.weight(1f))
}

@Composable
fun ColumnScope.SpacerFill(modifier: Modifier = Modifier) {
    Spacer(modifier = Modifier.weight(1f))
}
