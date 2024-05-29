package com.edugma.core.designSystem.atoms.spacer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SpacerHeight(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun ColumnScope.NavigationBarSpacer(height: Dp) {
    Spacer(modifier = Modifier.navigationBarsPadding().height(height))
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
fun LazyItemScope.NavigationBarSpacer(additionalHeight: Dp = 0.dp) {
    Spacer(modifier = Modifier.navigationBarsPadding().height(additionalHeight))
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
