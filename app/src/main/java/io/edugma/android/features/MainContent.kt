package io.edugma.android.features

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import io.edugma.android.appScreens
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.organism.navigationBar.EdNavigationBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.navigation.compose.getFullRawRoute
import io.edugma.features.base.core.navigation.compose.rememberNavController
import io.edugma.features.base.navigation.MainScreen
import io.edugma.features.base.navigation.nodes.NodesScreens
import org.koin.androidx.compose.getViewModel

val showNavBar = listOf(
    MainScreen.Home,
    MainScreen.Schedule,
    MainScreen.Account,
    MainScreen.Misc,
).map { it.route }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    viewModel: MainViewModel = getViewModel(),
) {
    val navController = rememberNavController(viewModel.router)

    Scaffold(
        bottomBar = { BottomNav(navController) },
        contentWindowInsets = WindowInsets(0.dp),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            NavHost(
                navController = navController,
                startDestination = NodesScreens.Main.getFullRawRoute(),
            ) {
                appScreens()
            }
//            Box(
//                Modifier.background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(
//                            EdTheme.colorScheme.background.withAlpha(0f),
//                            EdTheme.colorScheme.background,
//                        ),
//                    ),
//                ).fillMaxWidth()
//                    .height(10.dp)
//                    .align(Alignment.BottomCenter),
//            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomNav(navController: NavHostController) {
    val items = listOf(
        MainScreen.Home,
        MainScreen.Schedule,
        MainScreen.Account,
        MainScreen.Misc,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isNavigationBarVisible = remember(currentDestination) {
        currentDestination?.route in showNavBar
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
                val selected = currentDestination?.hierarchy?.any {
                    it.route == screen.route
                } == true
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
                            text = stringResource(screen.resourceId),
                            fontWeight = if (selected) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Medium
                            },
                            style = EdTheme.typography.labelMedium,
                        )
                    },
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}
