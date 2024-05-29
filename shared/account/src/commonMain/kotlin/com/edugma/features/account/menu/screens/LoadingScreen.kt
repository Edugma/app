package com.edugma.features.account.menu.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edugma.core.designSystem.atoms.loader.EdLoader
import com.edugma.core.designSystem.atoms.loader.EdLoaderSize
import com.edugma.core.designSystem.theme.EdTheme

@Composable
fun LoadingScreen() {
    Column(
        Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 20.dp, start = 4.dp, end = 4.dp),
    ) {
        Text(
            text = "Сервисы",
            style = EdTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 16.dp),
        )
        Box(modifier = Modifier.fillMaxSize()) {
            EdLoader(
                Modifier.align(Alignment.Center),
                EdLoaderSize.large,
            )
        }
    }
}
