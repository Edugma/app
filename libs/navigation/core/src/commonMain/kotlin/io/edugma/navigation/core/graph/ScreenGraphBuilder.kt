package io.edugma.navigation.core.graph

import androidx.compose.runtime.Composable
import io.edugma.navigation.core.screen.Screen
import io.edugma.navigation.core.screen.ScreenArgHolder
import io.edugma.navigation.core.screen.ScreenBundle

class ScreenGraphBuilder {
    val map = mutableMapOf<Screen, @Composable (ScreenBundle) -> Unit>()

    fun <T : Screen> screen(screen: T, content: @Composable ScreenArgHolder<T>.() -> Unit) {
        check(screen !in map) { "Screen with name ${screen.name} already in graph" }
        map[screen] = {
            content(ScreenArgHolder(it))
        }
    }

    // TODO Fix
    fun groupScreen(graph: Screen, startScreen: Screen, block: ScreenGraphBuilder.() -> Unit) {
        block()
    }
}

fun screenModule(block: ScreenGraphBuilder.() -> Unit): ScreenModule {
    return block
}

typealias ScreenModule = ScreenGraphBuilder.() -> Unit
