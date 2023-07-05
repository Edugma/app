package io.edugma.features.app.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.utils.LocalEdIconLoader
import io.edugma.core.designSystem.utils.LocalEdImageLoader
import io.edugma.navigation.core.compose.EdugmaTabNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    viewModel: MainViewModel = getViewModel(),
) {
    val (tabNavigator, isNavigationBarVisible) = rememberTabNavigator(viewModel)

    CompositionLocalProvider(
        LocalEdImageLoader provides viewModel.commonImageLoader,
        LocalEdIconLoader provides viewModel.iconImageLoader,
    ) {
        Scaffold(
            bottomBar = { BottomNav(tabNavigator, isNavigationBarVisible) },
            contentWindowInsets = WindowInsets(0.dp),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                EdugmaTabNavigation(tabNavigator)
            }
        }
    }
}
