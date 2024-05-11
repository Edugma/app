package io.edugma.features.app.presentation.main.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.organism.navigationBar.EdNavigationBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.navigation.AccountScreens
import io.edugma.core.navigation.HomeScreens
import io.edugma.core.navigation.MainDestination
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.core.navigation.core.TabMenuRouter
import io.edugma.core.navigation.core.rememberRouterNavigator
import io.edugma.core.navigation.misc.MiscMenuScreens
import io.edugma.features.app.presentation.main.MainViewModel
import io.edugma.navigation.core.compose.EdugmaNavigation
import io.edugma.navigation.core.graph.NavGraphBuilder
import io.edugma.navigation.core.navigator.ComposeNavigator
import io.edugma.navigation.core.destination.Destination
import io.edugma.navigation.core.destination.toBundle
import io.edugma.navigation.core.utils.getRoute

val showNavBar = listOf(
    HomeScreens.Main,
    ScheduleScreens.Menu,
    AccountScreens.Menu,
    MiscMenuScreens.Menu,
).map { it.getRoute() }

@Composable
internal fun rememberTabNavigator(viewModel: MainViewModel): Pair<ComposeNavigator, MutableState<Boolean>> {
    val tabNavigator = rememberRouterNavigator(viewModel.tabMenuRouter)

    val isNavigationBarVisible = remember {
        mutableStateOf(true)
    }

    return Pair(tabNavigator, isNavigationBarVisible)
}

@Composable
fun TabContent(
    edugmaNavigator: ComposeNavigator,
    start: Destination,
    builder: NavGraphBuilder.() -> Unit,
) {
    EdugmaNavigation(
        navigator = edugmaNavigator,
        start = start,
        builder = builder,
    )
}

val items = listOf(
    // MainScreen.Home,
    MainDestination.Schedule,
    MainDestination.Account,
    MainDestination.Misc,
)

@Composable
fun BottomNav(navigator: ComposeNavigator, router: TabMenuRouter, isVisible: State<Boolean>) {
    val navigationState by navigator.navController.currentBackStackEntryFlow.collectAsState(null)
    val currentDestination by remember {
        derivedStateOf { navigationState?.destination?.route }
    }
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = isVisible.value,
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
            height = 62.dp,
        ) {
            items.forEach { screen ->
                val selected = currentDestination == screen.getRoute()
                EdNavigationBarItem(
                    icon = {
                        Crossfade(
                            targetState = selected,
                            animationSpec = tween(durationMillis = 400),
                        ) { selected ->
                            if (selected) {
                                Icon(
                                    painter = painterResource(screen.getIcon(true)),
                                    contentDescription = null,
                                    modifier = Modifier.size(26.dp),
                                )
                            } else {
                                Icon(
                                    painter = painterResource(screen.getIcon(false)),
                                    contentDescription = null,
                                    modifier = Modifier.size(26.dp),
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
                            style = EdTheme.typography.labelSmall,
                        )
                    },
                    onClick = {
                        router.navigateTo(screen.toBundle()) {
                            popUpTo(screen) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
    }
}
