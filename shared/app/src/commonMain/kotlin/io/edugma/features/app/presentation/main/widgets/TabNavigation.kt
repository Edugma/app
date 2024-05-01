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
import androidx.compose.runtime.LaunchedEffect
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

import edugma.shared.core.icons.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import io.edugma.core.designSystem.atoms.label.EdLabel
import io.edugma.core.designSystem.organism.navigationBar.EdNavigationBar
import io.edugma.core.designSystem.theme.EdTheme
import io.edugma.core.navigation.AccountScreens
import io.edugma.core.navigation.HomeScreens
import io.edugma.core.navigation.MainScreen
import io.edugma.core.navigation.ScheduleScreens
import io.edugma.core.navigation.core.rememberRouterNavigator
import io.edugma.core.navigation.misc.MiscMenuScreens
import io.edugma.features.app.core.appScreens
import io.edugma.features.app.presentation.main.MainViewModel
import io.edugma.navigation.core.compose.EdugmaNavigation
import io.edugma.navigation.core.compose.rememberEdugmaNavigator
import io.edugma.navigation.core.graph.screenModule
import io.edugma.navigation.core.navigator.EdugmaNavigator
import io.edugma.navigation.core.navigator.ScreenUiState
import io.edugma.navigation.core.screen.bundleOf
import kotlinx.coroutines.flow.combine

val showNavBar = listOf(
    HomeScreens.Main,
    ScheduleScreens.Menu,
    AccountScreens.Menu,
    MiscMenuScreens.Menu,
).map { it.name }

@Composable
internal fun rememberTabNavigator(viewModel: MainViewModel): Pair<EdugmaNavigator, MutableState<Boolean>> {
    val homeNavigatorIsActive = remember {
        mutableStateOf(false)
    }
    val homeNavigator = rememberRouterNavigator(
        router = viewModel.router,
        screens = listOf(appScreens),
        firstScreen = HomeScreens.Main(),
        isActive = { homeNavigatorIsActive.value },
    )
    val scheduleNavigatorIsActive = remember {
        mutableStateOf(false)
    }

    // TODO: Save with LocalInstanceKeeperOwner
    val scheduleNavigator = rememberRouterNavigator(
        router = viewModel.router,
        screens = listOf(appScreens),
        firstScreen = ScheduleScreens.Menu(),
        isActive = { scheduleNavigatorIsActive.value },
    )
    val accountNavigatorIsActive = remember {
        mutableStateOf(false)
    }
    val accountNavigator = rememberRouterNavigator(
        router = viewModel.router,
        screens = listOf(appScreens),
        firstScreen = AccountScreens.Menu(),
        isActive = { accountNavigatorIsActive.value },
    )
    val miscNavigatorIsActive = remember {
        mutableStateOf(false)
    }
    val miscNavigator = rememberRouterNavigator(
        router = viewModel.router,
        screens = listOf(appScreens),
        firstScreen = MiscMenuScreens.Menu(),
        isActive = { miscNavigatorIsActive.value },
    )

    val tabNavigator = rememberEdugmaNavigator(
        screens = remember {
            listOf(
                screenModule {
//                    screen(MainScreen.Home) {
//                        TabContent(homeNavigator)
//                    }
                    screen(MainScreen.Schedule) {
                        TabContent(scheduleNavigator)
                    }
                    screen(MainScreen.Account) {
                        TabContent(accountNavigator)
                    }
                    screen(MainScreen.Misc) {
                        TabContent(miscNavigator)
                    }
                },
            )
        },
        firstScreen = MainScreen.Schedule.bundleOf(),
    )

    val isNavigationBarVisible = remember {
        mutableStateOf(true)
    }

    fun ScreenUiState.navBarIsVisible(tabScreen: ScreenUiState, mainScreen: MainScreen): Boolean {
        return tabScreen.screenBundle.screen == mainScreen &&
            this.screenBundle.screen.name in showNavBar
    }

    LaunchedEffect(key1 = Unit) {
        tabNavigator.state.collect {

            when (it.currentScreen.screenBundle.screen) {
                MainScreen.Home -> {
                    homeNavigatorIsActive.value = true
                    scheduleNavigatorIsActive.value = false
                    accountNavigatorIsActive.value = false
                    miscNavigatorIsActive.value = false
                }

                MainScreen.Schedule -> {
                    homeNavigatorIsActive.value = false
                    scheduleNavigatorIsActive.value = true
                    accountNavigatorIsActive.value = false
                    miscNavigatorIsActive.value = false
                }

                MainScreen.Account -> {
                    homeNavigatorIsActive.value = false
                    scheduleNavigatorIsActive.value = false
                    accountNavigatorIsActive.value = true
                    miscNavigatorIsActive.value = false
                }

                MainScreen.Misc -> {
                    homeNavigatorIsActive.value = false
                    scheduleNavigatorIsActive.value = false
                    accountNavigatorIsActive.value = false
                    miscNavigatorIsActive.value = true
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        combine(
            tabNavigator.state,
            homeNavigator.state,
            scheduleNavigator.state,
            accountNavigator.state,
            miscNavigator.state,
        ) { tabScreen, homeScreen, scheduleScreen, accountScreen, miscScreen ->
            homeScreen.currentScreen.navBarIsVisible(tabScreen.currentScreen, MainScreen.Home) ||
                scheduleScreen.currentScreen.navBarIsVisible(tabScreen.currentScreen, MainScreen.Schedule) ||
                accountScreen.currentScreen.navBarIsVisible(tabScreen.currentScreen, MainScreen.Account) ||
                miscScreen.currentScreen.navBarIsVisible(tabScreen.currentScreen, MainScreen.Misc)
        }.collect {
            isNavigationBarVisible.value = it
        }
    }
    return Pair(tabNavigator, isNavigationBarVisible)
}

@Composable
fun TabContent(edugmaNavigator: EdugmaNavigator) {
    EdugmaNavigation(edugmaNavigator)
}

val items = listOf(
    // MainScreen.Home,
    MainScreen.Schedule,
    MainScreen.Account,
    MainScreen.Misc,
)

@Composable
fun BottomNav(navController: EdugmaNavigator, isVisible: State<Boolean>) {
    val navigationState by navController.state.collectAsState()
    val currentDestination by remember {
        derivedStateOf { navigationState.currentScreen }
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
                val selected = currentDestination.screenBundle.screen.name == screen.name
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
                        navController.navigateTo(screen.bundleOf(), singleTop = true)
                    },
                )
            }
        }
    }
}
