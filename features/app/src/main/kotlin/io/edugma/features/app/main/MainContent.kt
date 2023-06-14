package io.edugma.features.app.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.edugma.core.arch.viewmodel.getViewModel
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.organism.navigationBar.EdNavigationBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.designSystem.utils.LocalEdIconLoader
import io.edugma.core.designSystem.utils.LocalEdImageLoader
import io.edugma.core.navigation.AccountScreens
import io.edugma.core.navigation.HomeScreens
import io.edugma.core.navigation.MainScreen
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.core.navigation.core.rememberRouterNavigator
import io.edugma.core.navigation.misc.MiscMenuScreens
import io.edugma.features.app.screens.appScreens
import io.edugma.navigation.core.compose.EdugmaNavigation
import io.edugma.navigation.core.compose.EdugmaTabNavigation
import io.edugma.navigation.core.compose.rememberEdugmaNavigator
import io.edugma.navigation.core.graph.screenModule
import io.edugma.navigation.core.navigator.EdugmaNavigator
import io.edugma.navigation.core.screen.ScreenBundle
import io.edugma.navigation.core.screen.bundleOf

val showNavBar = listOf(
    MainScreen.Home,
    MainScreen.Schedule,
    MainScreen.Account,
    MainScreen.Misc,
).map { it.name }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    viewModel: MainViewModel = getViewModel(),
) {
    val edugmaNavigator = rememberRouterNavigator(viewModel.router, listOf(appScreens))
    val tabNavigator = rememberEdugmaNavigator(
        remember {
            listOf(
                screenModule {
                    screen(MainScreen.Home) {
                        TabContent(edugmaNavigator, HomeScreens.Main())
                    }
                    screen(MainScreen.Schedule) {
                        TabContent(edugmaNavigator, ScheduleScreens.Menu())
                    }
                    screen(MainScreen.Account) {
                        TabContent(edugmaNavigator, AccountScreens.Menu())
                    }
                    screen(MainScreen.Misc) {
                        TabContent(edugmaNavigator, MiscMenuScreens.Menu())
                    }
                },
            )
        },
    )

    CompositionLocalProvider(
        LocalEdImageLoader provides viewModel.commonImageLoader,
        LocalEdIconLoader provides viewModel.iconImageLoader,
    ) {
        Scaffold(
            bottomBar = { BottomNav(tabNavigator) },
            contentWindowInsets = WindowInsets(0.dp),
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                EdugmaTabNavigation(tabNavigator, MainScreen.Home.bundleOf())
            }
        }
    }
}

@Composable
fun TabContent(edugmaNavigator: EdugmaNavigator, firstScreen: ScreenBundle) {
    EdugmaNavigation(edugmaNavigator, firstScreen)
}

@Composable
fun BottomNav(navController: EdugmaNavigator) {
    val items = listOf(
        MainScreen.Home,
        MainScreen.Schedule,
        MainScreen.Account,
        MainScreen.Misc,
    )
    val currentDestination by navController.currentScreen.collectAsState()
    val isNavigationBarVisible = remember(currentDestination) {
        currentDestination?.screenBundle?.screen?.name in showNavBar
    }
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = isNavigationBarVisible,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { 40.dp.roundToPx() }
        } + expandVertically() + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f,
        ),
        exit = slideOutVertically {
            with(density) { 40.dp.roundToPx() }
        } + shrinkVertically() + fadeOut(),

    ) {
        EdNavigationBar(
            tonalElevation = 3.dp,
            height = 80.dp,
        ) {
            items.forEach { screen ->
                val selected = currentDestination?.screenBundle?.screen?.name == screen.name
                NavigationBarItem(
                    icon = {
                        Crossfade(
                            targetState = selected,
                            animationSpec = tween(durationMillis = 400),
                        ) { selected ->
                            if (selected) {
                                Icon(
                                    painter = painterResource(screen.getIcon(true)),
                                    contentDescription = null,
                                )
                            } else {
                                Icon(
                                    painter = painterResource(screen.getIcon(false)),
                                    contentDescription = null,
                                )
                            }
                        }
                    },
                    selected = selected,
                    label = {
                        EdLabel(
                            text = stringResource(screen.tabNameId),
                            fontWeight = if (selected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Medium
                            },
                            style = EdTheme.typography.labelMedium,
                        )
                    },
                    onClick = {
                        navController.navigateTo(screen.bundleOf(), singleTop = true)
                    },
                )
            }
        }
    }
}
