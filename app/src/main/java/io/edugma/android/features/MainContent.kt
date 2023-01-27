package io.edugma.android.features

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import io.edugma.android.appScreens
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.features.base.core.navigation.compose.getRoute
import io.edugma.features.base.core.navigation.compose.rememberNavController
import io.edugma.features.base.core.utils.withAlpha
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
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = NodesScreens.Main.getRoute() + "?screen={screen}",
            ) {
                appScreens()
            }
            Box(
                Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            EdTheme.colorScheme.background.withAlpha(0f),
                            EdTheme.colorScheme.background,
                        ),
                    ),
                ).fillMaxWidth()
                    .height(10.dp)
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

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

    if (true || currentDestination?.route in showNavBar) {
        NavigationBar(
            tonalElevation = 0.dp,
        ) {
            items.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                NavigationBarItem(
                    icon = { Icon(painterResource(screen.getIcon(selected)), contentDescription = null) },
                    selected = selected,
                    label = { Text(stringResource(screen.resourceId)) },
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
                    modifier = Modifier.clip(EdTheme.shapes.medium),
                )
            }
        }
    }
}
