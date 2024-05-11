package io.edugma.navigation.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import io.edugma.navigation.core.destination.ArgumentsStore
import io.edugma.navigation.core.destination.Destination
import io.edugma.navigation.core.destination.NavArgs

val LocalArguments: ProvidableCompositionLocal<ArgumentsStore?> = staticCompositionLocalOf { null }

@Composable
fun <T : Destination> rememberNavArgs(destination: T): NavArgs<T> {
    val arguments = LocalArguments.current
        ?: throw IllegalStateException("Не найдены аргументы для $destination")

    return remember(destination, arguments) {
        NavArgs(destination, arguments)
    }
}
